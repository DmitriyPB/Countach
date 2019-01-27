package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.yandexresponse.geocoding.FeatureMember;
import com.testing.android.countach.data.yandexresponse.geocoding.GeoObject;
import com.testing.android.countach.data.yandexresponse.geocoding.YandexGeocodingResponse;

import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YandexGeodecodingService implements GeodecodingService {

    private YandexGeodecodingClient client;

    @Inject
    YandexGeodecodingService(YandexGeodecodingClient client) {this.client = client;}

    @NonNull
    @Override
    public String decode(double lat, double lon) {
        YandexGeocodingResponse response = null;
        try {
            response = client.decode(lon + "," + lat).execute().body();
        } catch (IOException e) {
            throw new RuntimeException("error while geodecoding", e);
        }
        return extractExactName(response);
    }

    @NonNull
    private String extractExactName(@Nullable YandexGeocodingResponse response) {
        for (FeatureMember featureMember : response.getResponse().getGeoObjectCollection().getFeatureMember()) {
            GeoObject geoObject = featureMember.getGeoObject();
            if ("exact".equals(geoObject.getMetaDataProperty().getGeocoderMetaData().getPrecision())) {
                return geoObject.getName();
            }
        }
        throw new RuntimeException("exact address not found");
    }
}
