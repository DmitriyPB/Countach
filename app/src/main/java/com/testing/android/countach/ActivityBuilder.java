package com.testing.android.countach;

import com.testing.android.countach.presentation.listing.ContactsListingFragment;

import androidx.fragment.app.Fragment;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityFragmentProvider.class)
    abstract MainActivity bindMainActivity();

    @Provides
    static Fragment provideInitialFragment() {
        return ContactsListingFragment.newInstance();
    }
}
