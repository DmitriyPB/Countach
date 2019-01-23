package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.room.AppDatabase;
import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.data.room.entity.ContactAddressRelationEntity;
import com.testing.android.countach.data.room.entity.ContactExtraEntity;
import com.testing.android.countach.data.room.entity.OrgEntity;
import com.testing.android.countach.data.yandexresponse.geocoding.FeatureMember;
import com.testing.android.countach.data.yandexresponse.geocoding.GeoObject;
import com.testing.android.countach.data.yandexresponse.geocoding.YandexGeocodingResponse;
import com.testing.android.countach.data.yandexresponse.orgsearch.CompanyMetaData;
import com.testing.android.countach.data.yandexresponse.orgsearch.Feature;
import com.testing.android.countach.data.yandexresponse.orgsearch.Geometry;
import com.testing.android.countach.data.yandexresponse.orgsearch.OrganizationsSearchResponse;
import com.testing.android.countach.data.yandexresponse.orgsearch.Properties_;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.Organization;
import com.testing.android.countach.domain.contactmap.ContactMapRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;

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
    public Completable submitContact(@NonNull String lookupKey, @NonNull Address currentAddress) {
        return Completable.fromCallable(() -> {
            return saveContactAddressAndOrganizations(lookupKey, currentAddress, searchForOrganizations(currentAddress.getAddressName()));
        });
    }

    @Override
    public Single<Address> loadContactAddress(String lookupKey) {
        return Single.fromCallable(() -> {
            ContactExtraEntity contact = contactExtraDao.getContactByLookup(lookupKey);
            if (contact != null) {
                return contact;
            }
            throw new NullPointerException("Contact not found");
        }).map(ContactExtraEntity::getAddress);
    }

    @Override
    public Single<String> decode(double lat, double lon) {
        return Single.fromCallable(() -> {
            Call<YandexGeocodingResponse> call = decodingService.decode(lon + "," + lat);
            return extractExactName(call.execute().body());
        });
    }

    @NonNull
    private String extractExactName(@Nullable YandexGeocodingResponse response) throws Exception {
        for (FeatureMember featureMember : response.getResponse().getGeoObjectCollection().getFeatureMember()) {
            GeoObject geoObject = featureMember.getGeoObject();
            if ("exact".equals(geoObject.getMetaDataProperty().getGeocoderMetaData().getPrecision())) {
                return geoObject.getName();
            }
        }
        throw new Exception("exact address not found");
    }

    private List<Organization> searchForOrganizations(@Nullable String addressName) throws IOException {
        if (addressName != null && !addressName.isEmpty()) {
            OrganizationsSearchResponse resp = organizationSearchService.searchForOrganizations(apiKey.getApiKey(), addressName)
                    .execute()
                    .body();
            return extractOrganizations(resp);
        }
        return Collections.emptyList();
    }

    private List<Organization> extractOrganizations(OrganizationsSearchResponse response) {
        if (response == null) throw new NullPointerException("extractOrganizations(null)");

        List<Feature> features = response.getFeatures();
        if (features == null) return Collections.emptyList();
        List<Organization> list = new ArrayList<>(features.size());

        for (Feature feature : features) {
            double lon;
            double lat;
            String name;
            long companyId;

            Geometry geometry = feature.getGeometry();
            if (geometry == null) break;
            List<Double> coordinates = geometry.getCoordinates();
            if (coordinates == null) break;
            Double lont = coordinates.get(0);
            if (lont == null) break;
            lon = lont;
            Double latt = coordinates.get(1);
            if (latt == null) break;
            lat = latt;

            Properties_ properties = feature.getProperties();
            if (properties == null) break;

            CompanyMetaData companyMetaData = properties.getCompanyMetaData();
            if (companyMetaData == null) break;
            name = companyMetaData.getName();
            if (name == null) break;
            String id = companyMetaData.getId();
            if (id == null) break;
            companyId = Long.parseLong(id);

            list.add(new OrgEntity(companyId, lat, lon, name));
        }
        return list;
    }

    private Integer saveContactAddressAndOrganizations(@NonNull String lookupKey, @NonNull Address contactAddress, @Nullable List<Organization> organizationsList) {
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

    private void insertAndLinkOrganizations(@Nullable List<Organization> organizationsList, int extraId) {
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
