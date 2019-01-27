package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.domain.Address;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface ContactMapRepository {
    Completable submitContact(@NonNull String lookupKey, @NonNull Address currentAddress);

    Single<Address> loadContactAddress(String lookupKey);

    Single<String> decode(double lat, double lon);

}
