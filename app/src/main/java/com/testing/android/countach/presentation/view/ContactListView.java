package com.testing.android.countach.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.testing.android.countach.data.Contact;

import java.util.List;

public interface ContactListView extends MvpView {
    void applyContacts(List<Contact> list);
}
