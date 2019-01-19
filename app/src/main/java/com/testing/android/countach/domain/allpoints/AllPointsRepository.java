package com.testing.android.countach.domain.allpoints;

import com.testing.android.countach.domain.PinPoint;

import java.util.List;

import io.reactivex.Observable;

public interface AllPointsRepository {
    Observable<List<? extends PinPoint>> loadPoints();
}
