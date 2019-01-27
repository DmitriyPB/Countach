package com.testing.android.countach.presentation.allpoints;

import javax.inject.Provider;

import dagger.android.AndroidInjector;

public class AllPointsFragmentAndroidInjector implements AndroidInjector<AllPointsFragment> {

    private Provider<AllPointsPresenter> presenterProvider;

    public AllPointsFragmentAndroidInjector(Provider<AllPointsPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public void inject(AllPointsFragment instance) {
        instance.presenterProvider = presenterProvider;
    }
}
