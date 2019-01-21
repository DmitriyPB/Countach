package com.testing.android.countach.presentation.listing;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;

abstract class ContactsListingPresenter extends MvpPresenter<ContactsListingView> {
    abstract void loadContacts(@Nullable String query);
}
