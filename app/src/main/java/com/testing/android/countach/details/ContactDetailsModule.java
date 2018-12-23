package com.testing.android.countach.details;

import dagger.Binds;
import dagger.Module;

@Module
public interface ContactDetailsModule {

    @Binds
    @ContactDetailsScope
    ContactDetailsPresenter providePresenter(ContactDetailsPresenterImpl presenter);
}

