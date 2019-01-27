package com.testing.android.countach.rxschedulers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class SequentialSchedulersProvider implements SchedulersProvider {
    @Override
    public Scheduler ioScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler uiScheduler() {
        return Schedulers.trampoline();
    }
}
