package com.testing.android.countach.presentation.organizationsearch;

import com.testing.android.countach.data.organizationsearch.OrgsListingRepositoryImpl;
import com.testing.android.countach.domain.organizationsearch.OrgsListingRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface OrgsListingModule {

    @Binds
    @OrgsListingScope
    OrgsListingPresenter providePresenter(OrgsListingPresenterImpl presenter);

    @Binds
    @OrgsListingScope
    OrgsListingRepository provideRepository(OrgsListingRepositoryImpl repository);

}
