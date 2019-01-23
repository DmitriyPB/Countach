package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.data.yandexresponse.orgsearch.OrganizationsSearchResponse;

import java.io.IOException;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrganizationSearchService {
    @GET("https://search-maps.yandex.ru/v1/?type=biz&lang=ru_RU")
    Call<OrganizationsSearchResponse> searchForOrganizations(@NonNull @Query("apikey") String apiKey,
                                                             @NonNull @Query("text") String text
    ) throws IOException;
}
