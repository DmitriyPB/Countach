package com.testing.android.countach;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
final class AppModule {
    private Context context;

    AppModule(Application application) {
        context = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }
}
