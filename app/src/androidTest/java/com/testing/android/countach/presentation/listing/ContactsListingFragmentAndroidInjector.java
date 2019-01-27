package com.testing.android.countach.presentation.listing;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class ContactsListingFragmentAndroidInjector implements AndroidInjector<ContactsListingFragment> {

    private Provider<ContactsListingPresenter> presenterProvider;

    public ContactsListingFragmentAndroidInjector(Provider<ContactsListingPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public void inject(ContactsListingFragment instance) {
        instance.presenterProvider = presenterProvider;
    }
}
