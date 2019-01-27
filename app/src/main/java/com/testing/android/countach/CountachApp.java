package com.testing.android.countach;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import androidx.annotation.VisibleForTesting;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

final public class CountachApp extends Application implements HasActivityInjector {

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Inject
    public DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    public CountachApp() {
        createAppComponent();
    }

    private void createAppComponent() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .application(this)
                .build().inject(this);
    }

    public static CountachApp get(Context context) {
        return ((CountachApp) context.getApplicationContext());
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
