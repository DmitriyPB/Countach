package com.testing.android.countach.presentation.contactmap;

import com.testing.android.countach.data.contactmap.ApiKey;
import com.testing.android.countach.data.contactmap.ApiKeyFromResources;
import com.testing.android.countach.data.contactmap.ContactMapRepositoryImpl;
import com.testing.android.countach.data.contactmap.GeodecodingService;
import com.testing.android.countach.data.contactmap.OrganizationSearchService;
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
    static GeodecodingService provideGeodecodeService(Retrofit retrofit) {
        return retrofit.create(GeodecodingService.class);
    }

    @Provides
    @ContactMapScope
    static OrganizationSearchService provideOrganizationSearchService(Retrofit retrofit) {
        return retrofit.create(OrganizationSearchService.class);
    }

    @Binds
    @ContactMapScope
    abstract ApiKey provideApiKey(ApiKeyFromResources key);

}
