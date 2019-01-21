package com.testing.android.countach;

import com.testing.android.countach.presentation.details.ContactDetailsFragment;
import com.testing.android.countach.presentation.details.ContactDetailsModule;
import com.testing.android.countach.presentation.details.ContactDetailsScope;
import com.testing.android.countach.presentation.listing.ContactsListingFragment;
import com.testing.android.countach.presentation.listing.ContactsListingModule;
import com.testing.android.countach.presentation.listing.ContactsListingScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityFragmentProvider {
    @ContactsListingScope
    @ContributesAndroidInjector(modules = ContactsListingModule.class)
    abstract ContactsListingFragment provideContactsListingFragment();

    @ContactDetailsScope
    @ContributesAndroidInjector(modules = ContactDetailsModule.class)
    abstract ContactDetailsFragment provideContactDetailsFragment();
}
