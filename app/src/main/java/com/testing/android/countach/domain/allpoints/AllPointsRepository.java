package com.testing.android.countach.domain.allpoints;

import com.testing.android.countach.domain.PinPoint;

import java.util.List;

import io.reactivex.Single;

public interface AllPointsRepository {
    Single<List<? extends PinPoint>> loadPoints();
}
