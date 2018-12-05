package com.testing.android.countach;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final public class AppExecutors {

    private final ExecutorService worker;
    private final Executor main;

    public AppExecutors(ExecutorService worker, Executor main) {
        this.worker = worker;
        this.main = main;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    public ExecutorService worker() {
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