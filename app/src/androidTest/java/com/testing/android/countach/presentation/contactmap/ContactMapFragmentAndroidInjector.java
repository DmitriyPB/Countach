package com.testing.android.countach.presentation.contactmap;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class ContactMapFragmentAndroidInjector implements AndroidInjector<ContactMapFragment> {

    private Provider<ContactMapPresenter> presenterProvider;

    public ContactMapFragmentAndroidInjector(Provider<ContactMapPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public void inject(ContactMapFragment instance) {
        instance.presenterProvider = presenterProvider;
    }
}
