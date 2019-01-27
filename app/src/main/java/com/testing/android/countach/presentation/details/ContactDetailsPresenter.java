package com.testing.android.countach.presentation.details;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;

public abstract class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    public abstract void loadContactDetails(@NonNull String lookupKey);
}
