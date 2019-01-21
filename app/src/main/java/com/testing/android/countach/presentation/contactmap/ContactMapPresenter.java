package com.testing.android.countach.presentation.contactmap;

import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.domain.PinPoint;

abstract class ContactMapPresenter extends MvpPresenter<ContactMapView> {

    abstract void loadPinPointForContact(String lookupKey);

    abstract void submitPinPoint();

    abstract void submitGeodecoding(PinPoint pinPoint);
}