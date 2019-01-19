package com.testing.android.countach.presentation.allpoints;

import com.arellomobile.mvp.MvpPresenter;

abstract class AllPointsPresenter extends MvpPresenter<AllPointsView> {
    abstract void loadAllPinPoints();
}
