package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.room.AppDatabase;
import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.data.room.entity.ContactAddressRelationEntity;
import com.testing.android.countach.data.room.entity.ContactExtraEntity;
import com.testing.android.countach.data.room.entity.OrgEntity;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.Organization;
import com.testing.android.countach.domain.contactmap.ContactMapRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

final public class ContactMapRepositoryImpl implements ContactMapRepository {

    private final ApiKey apiKey;
    private final GeodecodingService decodingService;
    private final OrganizationSearchService organizationSearchService;
    private final ContactExtraDao contactExtraDao;
    private final OrganizationDao organizationDao;
    private final OrgContactRelationDao orgContactRelationDao;
    private final AppDatabase db;

    @Inject
    ContactMapRepositoryImpl(ApiKey apiKey,
                             GeodecodingService decodingService,
                             OrganizationSearchService organizationSearchService,
                             ContactExtraDao contactExtraDao,
                             OrganizationDao organizationDao,
                             OrgContactRelationDao orgContactRelationDao,
                             AppDatabase db) {
        this.apiKey = apiKey;
        this.decodingService = decodingService;
        this.organizationSearchService = organizationSearchService;
        this.contactExtraDao = contactExtraDao;
        this.organizationDao = organizationDao;
        this.orgContactRelationDao = orgContactRelationDao;
        this.db = db;
    }

    @Override
    public Single<Integer> submitContact(String lookupKey, Address currentAddress, List<Organization> currentOrganizationsList) {
        return Single.fromCallable(() -> saveContactAddressAndOrganizations(lookupKey, currentAddress, currentOrganizationsList));
    }

    @Override
    public Flowable<Address> loadContactAddress(String lookupKey) {
        return Flowable.fromCallable(() -> {
            ContactExtraEntity contact = contactExtraDao.getContactByLookup(lookupKey);
            if (contact != null) {
                return contact;
            }
            throw new NullPointerException("Contact not found");
        }).map(ContactExtraEntity::getAddress);
    }

    @Override
    public Single<String> decode(double lat, double lon) {
        return decodingService.decode(lat, lon);
    }

    @Override
    public Single<List<Organization>> searchForOrganizations(String addressName) {
        return organizationSearchService.searchForOrganizations(apiKey.get(), addressName);
    }

    private Integer saveContactAddressAndOrganizations(String lookupKey, Address contactAddress, List<Organization> organizationsList) {
        db.runInTransaction(() -> {
            ContactExtraEntity extra = contactExtraDao.getContactByLookup(lookupKey);
            int extraId;
            if (extra == null) {
                extraId = contactExtraDao.insertContact(new ContactExtraEntity(lookupKey, new AddressBean(contactAddress))).intValue();
            } else {
                extraId = contactExtraDao.insertContact(new ContactExtraEntity(extra.getId(), lookupKey, new AddressBean(contactAddress))).intValue();
                orgContactRelationDao.deleteRelationsByContactId(extra.getId());
                organizationDao.deleteStaleOrganization();
            }
            insertAndLinkOrganizations(organizationsList, extraId);
        });
        return 0;
    }

    private void insertAndLinkOrganizations(List<Organization> organizationsList, int extraId) {
        if (organizationsList != null && !organizationsList.isEmpty()) {
            List<Long> orgIds = organizationDao.insertOrganizations(OrgEntity.listFrom(organizationsList));
            if (orgIds != null) {
                for (Long orgId : orgIds) {
                    orgContactRelationDao.insertRelation(new ContactAddressRelationEntity(extraId, orgId.intValue()));
                }
            }
        }
    }
}
