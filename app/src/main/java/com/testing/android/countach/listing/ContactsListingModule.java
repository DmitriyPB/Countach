package com.testing.android.countach.listing;

import dagger.Binds;
import dagger.Module;

@Module
public interface ContactsListingModule {

    @Binds
    @ContactsListingScope
    ContactsListingPresenter providePresenter(ContactsListingPresenterImpl presenter);
}
