package com.testing.android.countach.details;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.domain.Contact;

public interface ContactDetailsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void applyContact(Contact contact);
}
