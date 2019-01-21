package com.testing.android.countach.presentation.details;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;

abstract class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    abstract void loadContactDetails(@NonNull String lookupKey);
}
