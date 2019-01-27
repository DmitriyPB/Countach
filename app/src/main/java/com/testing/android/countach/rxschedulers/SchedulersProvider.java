package com.testing.android.countach.rxschedulers;

import io.reactivex.Scheduler;

public interface SchedulersProvider {
    Scheduler ioScheduler();

    Scheduler uiScheduler();
}
