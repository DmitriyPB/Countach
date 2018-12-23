package com.testing.android.countach;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract Repository provideRepo(RepositoryImpl repository);
}
