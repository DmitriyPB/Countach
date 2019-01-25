package com.testing.android.countach;

import com.testing.android.countach.presentation.allpoints.AllPointsFragment;
import com.testing.android.countach.presentation.allpoints.AllPointsModule;
import com.testing.android.countach.presentation.allpoints.AllPointsScope;
import com.testing.android.countach.presentation.contactmap.ContactMapFragment;
import com.testing.android.countach.presentation.contactmap.ContactMapModule;
import com.testing.android.countach.presentation.contactmap.ContactMapScope;
import com.testing.android.countach.presentation.details.ContactDetailsFragment;
import com.testing.android.countach.presentation.details.ContactDetailsModule;
import com.testing.android.countach.presentation.details.ContactDetailsScope;
import com.testing.android.countach.presentation.listing.ContactsListingFragment;
import com.testing.android.countach.presentation.listing.ContactsListingModule;
import com.testing.android.countach.presentation.listing.ContactsListingScope;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingFragment;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingModule;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingScope;

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

    @AllPointsScope
    @ContributesAndroidInjector(modules = AllPointsModule.class)
    abstract AllPointsFragment provideAllPointsFragment();

    @ContactMapScope
    @ContributesAndroidInjector(modules = ContactMapModule.class)
    abstract ContactMapFragment provideContactMapFragment();

    @OrgsListingScope
    @ContributesAndroidInjector(modules = OrgsListingModule.class)
    abstract OrgsListingFragment provideOrgListingFragment();
}
