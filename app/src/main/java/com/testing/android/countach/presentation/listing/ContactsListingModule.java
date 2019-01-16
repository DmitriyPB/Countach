package com.testing.android.countach.presentation.listing;

import com.testing.android.countach.data.listing.ContactsListingRepositoryImpl;
import com.testing.android.countach.domain.listing.ContactsListingInteractor;
import com.testing.android.countach.domain.listing.ContactsListingModel;
import com.testing.android.countach.domain.listing.ContactsListingRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface ContactsListingModule {

    @Binds
    @ContactsListingScope
    ContactsListingPresenter providePresenter(ContactsListingPresenterImpl presenter);

    @Binds
    @ContactsListingScope
    ContactsListingInteractor provideInteractor(ContactsListingModel interactor);

    @Binds
    @ContactsListingScope
    ContactsListingRepository provideRepository(ContactsListingRepositoryImpl repository);
}
