package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.room.entity.OrgEntity;
import com.testing.android.countach.data.yandexresponse.orgsearch.CompanyMetaData;
import com.testing.android.countach.data.yandexresponse.orgsearch.Feature;
import com.testing.android.countach.data.yandexresponse.orgsearch.Geometry;
import com.testing.android.countach.data.yandexresponse.orgsearch.OrganizationsSearchResponse;
import com.testing.android.countach.data.yandexresponse.orgsearch.Properties_;
import com.testing.android.countach.domain.Organization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class YandexOrganizationsSearchService implements OrganizationsSearchService {
    private YandexOrganizationsSearchClient client;

    @Inject
    YandexOrganizationsSearchService(YandexOrganizationsSearchClient client) {this.client = client;}

    @NonNull
    @Override
    public List<Organization> searchForOrganizations(@NonNull String apiKey, @NonNull String query) {
        try {
            OrganizationsSearchResponse response = client.searchForOrganizations(apiKey, query).execute().body();
            return extractOrganizations(response);
        } catch (IOException e) {
            throw new RuntimeException("error while searching for organizations : " + query, e);
        }
    }

    private List<Organization> extractOrganizations(OrganizationsSearchResponse response) {
        if (response == null) throw new IllegalArgumentException("extractOrganizations(null)");

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
}
