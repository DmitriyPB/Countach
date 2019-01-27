package com.testing.android.countach;

import android.app.Application;
import android.content.Context;

import com.testing.android.countach.rxschedulers.AndroidSchedulersProvider;
import com.testing.android.countach.rxschedulers.SchedulersProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    private Context context;

    public AppModule(Application application) {
        context = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    SchedulersProvider provideSchedulers() {
        return new AndroidSchedulersProvider();
    }
}
