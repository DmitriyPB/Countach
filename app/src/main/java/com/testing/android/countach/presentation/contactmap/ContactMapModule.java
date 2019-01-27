package com.testing.android.countach.presentation.contactmap;

import com.testing.android.countach.data.contactmap.ApiKey;
import com.testing.android.countach.data.contactmap.ApiKeyFromResources;
import com.testing.android.countach.data.contactmap.ContactMapRepositoryImpl;
import com.testing.android.countach.data.contactmap.GeodecodingService;
import com.testing.android.countach.data.contactmap.OrganizationsSearchService;
import com.testing.android.countach.data.contactmap.YandexGeodecodingClient;
import com.testing.android.countach.data.contactmap.YandexGeodecodingService;
import com.testing.android.countach.data.contactmap.YandexOrganizationsSearchClient;
import com.testing.android.countach.data.contactmap.YandexOrganizationsSearchService;
import com.testing.android.countach.domain.contactmap.ContactMapInteractor;
import com.testing.android.countach.domain.contactmap.ContactMapModel;
import com.testing.android.countach.domain.contactmap.ContactMapRepository;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public abstract class ContactMapModule {
    @Binds
    @ContactMapScope
    abstract ContactMapPresenter providePresenter(ContactMapPresenterImpl presenter);

    @Binds
    @ContactMapScope
    abstract ContactMapRepository provideRepository(ContactMapRepositoryImpl repository);

    @Binds
    @ContactMapScope
    abstract ContactMapInteractor provideInteractor(ContactMapModel model);

    @Provides
    @ContactMapScope
    static YandexGeodecodingClient provideGeodecodeService(Retrofit retrofit) {
        return retrofit.create(YandexGeodecodingClient.class);
    }

    @Provides
    @ContactMapScope
    static YandexOrganizationsSearchClient provideOrganizationSearchService(Retrofit retrofit) {
        return retrofit.create(YandexOrganizationsSearchClient.class);
    }

    @Binds
    @ContactMapScope
    abstract OrganizationsSearchService provideOrganizationsSearchService(YandexOrganizationsSearchService service);

    @Binds
    @ContactMapScope
    abstract GeodecodingService provideGeodecodingService(YandexGeodecodingService service);

    @Binds
    @ContactMapScope
    abstract ApiKey provideApiKey(ApiKeyFromResources key);

}
