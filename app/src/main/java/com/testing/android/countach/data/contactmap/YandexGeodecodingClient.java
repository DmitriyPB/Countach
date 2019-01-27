package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.yandexresponse.geocoding.YandexGeocodingResponse;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexGeodecodingClient {

    @GET("https://geocode-maps.yandex.ru/1.x/?format=json")
    Call<YandexGeocodingResponse> decode(
            @NonNull @Query("geocode") String query
    );
}
