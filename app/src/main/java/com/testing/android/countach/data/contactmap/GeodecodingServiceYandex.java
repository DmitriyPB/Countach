package com.testing.android.countach.data.contactmap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final public class GeodecodingServiceYandex implements GeodecodingService {
    private OkHttpClient client;

    @Inject
    GeodecodingServiceYandex(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Single<String> decode(Double lat, Double lon) {
        return Single.fromCallable(() -> decodeSync(lat, lon));
    }

    @Nullable
    private String decodeSync(Double lat, Double lon) throws ParseException, IOException {
        Request request = new Request.Builder()
                .url("https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + lon + "," + lat)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String stringResp = response.body().string();
            return extractExactName(stringResp);
        }
    }

    private String extractExactName(String json) throws ParseException {
        JSONObject parsed = (JSONObject) new JSONParser().parse(json);
        JSONObject response = (JSONObject) parsed.get("response");
        JSONObject collection = (JSONObject) response.get("GeoObjectCollection");
        JSONArray featureMember = (JSONArray) collection.get("featureMember");
        for (Object el : featureMember) {
            JSONObject geoObject = (JSONObject) ((JSONObject) el).get("GeoObject");
            JSONObject metaDataProperty = (JSONObject) geoObject.get("metaDataProperty");
            JSONObject geocoderMetaData = (JSONObject) metaDataProperty.get("GeocoderMetaData");
            String precision = (String) geocoderMetaData.get("precision");
            if ("exact".equals(precision)) {
                String name = (String) geoObject.get("name");
                return name != null ? name : "";
            }
        }
        return "";
    }
}
