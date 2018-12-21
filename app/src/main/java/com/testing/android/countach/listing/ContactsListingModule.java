package com.testing.android.countach.listing;

import com.testing.android.countach.IRepository;

import dagger.Module;
import dagger.Provides;

@Module
final public class ContactsListingModule {

    @Provides
    @ContactsListingScope
    ContactsListingPresenter providePresenter(IRepository repo) {
        return new ContactsListingPresenter(repo);
    }
}
