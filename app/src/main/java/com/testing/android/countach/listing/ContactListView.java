package com.testing.android.countach.listing;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.Contact;

import java.util.List;

public interface ContactListView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void applyContacts(List<Contact> list);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void loadContactsWithPermissionCheck();
}
