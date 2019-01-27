package com.testing.android.countach.presentation.organizationsearch;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;

abstract class OrgsListingPresenter extends MvpPresenter<OrgsListingView> {

    abstract void loadOrganizationByQuery(@Nullable String query);
}
