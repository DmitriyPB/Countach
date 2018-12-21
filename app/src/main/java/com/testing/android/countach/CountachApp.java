package com.testing.android.countach;

import android.app.Application;
import android.content.Context;

final public class CountachApp extends Application {

    private AppComponent appComponent;

    public CountachApp() {
        appComponent = createAppComponent();
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static CountachApp get(Context context) {
        return ((CountachApp) context.getApplicationContext());
    }
}
