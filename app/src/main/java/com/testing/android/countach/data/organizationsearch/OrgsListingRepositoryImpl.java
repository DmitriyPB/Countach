package com.testing.android.countach.data.organizationsearch;

import android.provider.ContactsContract;

import com.testing.android.countach.data.ContactsDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.Organization;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;
import com.testing.android.countach.domain.organizationsearch.OrgsListingRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

final public class OrgsListingRepositoryImpl implements OrgsListingRepository {

    private final OrganizationDao organizationDao;
    private final OrgContactRelationDao orgContactRelationDao;
    private final ContactsDao contactsDao;

    @Inject
    OrgsListingRepositoryImpl(OrganizationDao organizationDao,
                              OrgContactRelationDao orgContactRelationDao,
                              ContactsDao contactsDao) {
        this.organizationDao = organizationDao;
        this.orgContactRelationDao = orgContactRelationDao;
        this.contactsDao = contactsDao;
    }

    @Override
    public Single<List<OrgSearchResult>> loadOrganizationsList(String query) {
        return Single.fromCallable(() -> {
            List<? extends Organization> organizations = organizationDao.loadAllOrganizations("%" + query + "%");
            if (organizations != null) {
                return organizations;
            }
            return new ArrayList<Organization>();
        }).map(this::enrichWithContacts);
    }

    private List<OrgSearchResult> enrichWithContacts(List<? extends Organization> organizations) {
        List<OrgSearchResult> res = new ArrayList<>(organizations.size());
        for (Organization org : organizations) {
            List<String> lookups = orgContactRelationDao.getLookupsByOrgId(org.getOrgId().intValue());
            System.out.println("org : " + org);
            if (lookups != null) {
                System.out.println("lookups : " + Arrays.toString(lookups.toArray(new String[]{})));
                List<Contact> contacts = selectContactsSetBy(lookups);
                res.add(new OrgSearchResultBean(org, contacts));
            } else {
                System.out.println("lookups null");
            }
        }
        return res;
    }

    private List<Contact> selectContactsSetBy(List<String> lookups) {
        String[] SELECTION_ARGS = lookups.toArray(new String[]{});
        String queryPlaceHolders = buildPlaceHolders(SELECTION_ARGS);
        String SELECTION_CONTACT_LIST = ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY + " IN (" + queryPlaceHolders + ")";
        return contactsDao.queryContacts(SELECTION_CONTACT_LIST, SELECTION_ARGS, ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY);
    }

    private String buildPlaceHolders(String[] SELECTION_ARGS) {
        int length = SELECTION_ARGS.length;
        StringBuilder inList = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                inList.append(",");
            }
            inList.append("?");
        }
        return inList.toString();
    }
}
