package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.yandexresponse.geocoding.YandexGeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeodecodingService {

    @GET("https://geocode-maps.yandex.ru/1.x/?format=json")
    Call<YandexGeocodingResponse> decode(
            @Query("geocode") String query
    );
}
