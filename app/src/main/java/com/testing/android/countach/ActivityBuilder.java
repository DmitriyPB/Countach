package com.testing.android.countach;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityFragmentProvider.class)
    abstract MainActivity bindMainActivity();
}
