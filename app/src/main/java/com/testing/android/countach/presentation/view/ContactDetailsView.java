package com.testing.android.countach.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.testing.android.countach.data.Contact;

public interface ContactDetailsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void  applyContact(
            Contact contact
    );
}
