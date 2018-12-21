package com.testing.android.countach.details;

import com.testing.android.countach.IRepository;

import dagger.Module;
import dagger.Provides;

@Module
final public class ContactDetailsModule {

    @Provides
    @ContactDetailsScope
    ContactDetailsPresenter providePresenter(IRepository repo) {
        return new ContactDetailsPresenter(repo);
    }
}
