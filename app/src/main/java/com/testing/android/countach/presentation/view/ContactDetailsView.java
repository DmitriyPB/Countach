package com.testing.android.countach.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.testing.android.countach.data.Contact;

public interface ContactDetailsView extends MvpView {
    void applyContact(Contact contact);
}
