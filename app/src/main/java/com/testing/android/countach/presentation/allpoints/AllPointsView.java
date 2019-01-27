package com.testing.android.countach.presentation.allpoints;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.PinPoint;

import java.util.List;

public interface AllPointsView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void applyPinPoints(@Nullable List<? extends PinPoint> points);
}
