package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.PinPoint;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ContactMapInteractor {
    Completable submitContact(String lookupKey, Address currentAddress);

    Single<Address> loadContactAddress(String lookupKey);

    Single<Address> geodecode(PinPoint pinPoint);
}
