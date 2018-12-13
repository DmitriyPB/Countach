package com.testing.android.countach;

import android.app.Application;
import android.content.Context;

final public class CountachApp extends Application {

    public Repository repository;

    public CountachApp() {
        repository = new Repository(this);
    }

    public static CountachApp get(Context context) {
        return ((CountachApp) context.getApplicationContext());
    }

    public Repository getRepository() {
        return repository;
    }
}
