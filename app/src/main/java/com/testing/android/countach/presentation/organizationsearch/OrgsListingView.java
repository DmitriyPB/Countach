package com.testing.android.countach.presentation.organizationsearch;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;

import java.util.List;

public interface OrgsListingView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLoadingIndicator(boolean show);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void applyResult(List<OrgSearchResult> orgs);
}
