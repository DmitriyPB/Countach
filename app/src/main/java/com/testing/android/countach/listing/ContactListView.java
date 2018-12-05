package com.testing.android.countach.listing;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.Contact;

import java.util.List;

public interface ContactListView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void applyContacts(List<Contact> list);
}
