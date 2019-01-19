package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.PinPoint;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ContactMapInteractor {
    Single<Integer> submitContact();

    Flowable<Address> loadContactAddress(String lookupKey);

    Single<Address> geodecode(PinPoint pinPoint);
}
