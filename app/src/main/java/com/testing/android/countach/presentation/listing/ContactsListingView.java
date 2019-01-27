package com.testing.android.countach.presentation.listing;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.Contact;

import java.util.List;

public interface ContactsListingView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void applyContacts(List<Contact> list);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void loadContactsWithPermissionCheck();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLoadingIndicator(boolean show);
}
