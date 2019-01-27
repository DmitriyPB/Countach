package com.testing.android.countach.presentation.organizationsearch;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class OrgsListingFragmentAndroidInjector implements AndroidInjector<OrgsListingFragment> {

    private Provider<OrgsListingPresenter> presenterProvider;

    public OrgsListingFragmentAndroidInjector(Provider<OrgsListingPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public void inject(OrgsListingFragment instance) {
        instance.presenterProvider = presenterProvider;
    }
}
