package com.testing.android.countach.presentation.contactmap;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.Address;

public interface ContactMapView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void applyContactPinPoint(Address address);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onSubmitSuccess();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showGeodecodingIndicator(boolean show);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onGeodecodingFinished(Address point);
}
