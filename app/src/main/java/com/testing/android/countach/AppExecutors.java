package com.testing.android.countach;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

final public class AppExecutors {

    private final Executor worker;
    private final Executor main;

    public AppExecutors(Executor worker, Executor main) {
        this.worker = worker;
        this.main = main;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    public Executor worker() {
        return worker;
    }

    public Executor ui() {
        return main;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}