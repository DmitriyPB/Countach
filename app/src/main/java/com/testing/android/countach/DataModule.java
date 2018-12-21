package com.testing.android.countach;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
final class DataModule {

    @Provides
    @Singleton
    IRepository provideRepo(Context context) {
        return new Repository(context);
    }
}
