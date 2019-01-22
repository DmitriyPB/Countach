package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.room.entity.OrgEntity;
import com.testing.android.countach.domain.Organization;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final public class OrganizationSearchServiceYandex implements OrganizationSearchService {

    private OkHttpClient client;

    @Inject
    OrganizationSearchServiceYandex(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public List<Organization> searchForOrganizations(@NonNull String apiKey, @NonNull String text) throws IOException, ParseException {
        return searchReal(apiKey, text);
    }

    private List<Organization> searchReal(String apiKey, String text) throws ParseException, IOException {
        String url = "https://search-maps.yandex.ru/v1/?text=" + text + "&type=biz&lang=ru_RU&apikey=" + apiKey;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String stringResp = response.body().string();
            return extractOrganizations(stringResp);
        }
    }

    private List<Organization> extractOrganizations(String json) throws ParseException {
        JSONObject parsed = (JSONObject) new JSONParser().parse(json);
        JSONArray featureMember = (JSONArray) parsed.get("features");
        List<Organization> list = new ArrayList<>(featureMember.size());
        if (!featureMember.isEmpty()) {
            for (Object el : featureMember) {
                JSONObject elJson = (JSONObject) el;
                JSONObject geometry = (JSONObject) elJson.get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                JSONObject properties = (JSONObject) elJson.get("properties");
                JSONObject companyMetaData = (JSONObject) properties.get("CompanyMetaData");
                String idStr = (String) companyMetaData.get("id");
                if (idStr != null) {
                    Long orgID = Long.valueOf(idStr);
                    String name = (String) properties.get("name");
                    Double lon = (Double) coordinates.get(0);
                    Double lat = (Double) coordinates.get(1);
                    list.add(new OrgEntity(orgID, lat, lon, name));
                }
            }
        }
        return list;
    }
}
