package com.testing.android.countach.presentation.details;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class ContactDetailsFragmentAndroidInjector implements AndroidInjector<ContactDetailsFragment> {

    private Provider<ContactDetailsPresenter> presenterProvider;

    public ContactDetailsFragmentAndroidInjector(Provider<ContactDetailsPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public void inject(ContactDetailsFragment instance) {
        instance.presenterProvider = presenterProvider;
    }
}
