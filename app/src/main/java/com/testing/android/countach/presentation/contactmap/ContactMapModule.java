package com.testing.android.countach.presentation.contactmap;

import com.testing.android.countach.data.contactmap.ApiKey;
import com.testing.android.countach.data.contactmap.ApiKeyFromResources;
import com.testing.android.countach.data.contactmap.ContactMapRepositoryImpl;
import com.testing.android.countach.data.contactmap.GeodecodingService;
import com.testing.android.countach.data.contactmap.GeodecodingServiceYandex;
import com.testing.android.countach.data.contactmap.OrganizationSearchService;
import com.testing.android.countach.data.contactmap.OrganizationSearchServiceYandex;
import com.testing.android.countach.domain.contactmap.ContactMapInteractor;
import com.testing.android.countach.domain.contactmap.ContactMapModel;
import com.testing.android.countach.domain.contactmap.ContactMapRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface ContactMapModule {
    @Binds
    @ContactMapScope
    ContactMapPresenter providePresenter(ContactMapPresenterImpl presenter);

    @Binds
    @ContactMapScope
    ContactMapRepository provideRepository(ContactMapRepositoryImpl repository);

    @Binds
    @ContactMapScope
    ContactMapInteractor provideInteractor(ContactMapModel model);

    @Binds
    @ContactMapScope
    GeodecodingService provideGeodecodeService(GeodecodingServiceYandex service);

    @Binds
    @ContactMapScope
    OrganizationSearchService provideOrganizationSearchService(OrganizationSearchServiceYandex service);

    @Binds
    @ContactMapScope
    ApiKey provideApiKey(ApiKeyFromResources key);

}
