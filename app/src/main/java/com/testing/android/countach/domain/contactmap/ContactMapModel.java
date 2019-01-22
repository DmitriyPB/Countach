package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.PinPoint;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.Single;

final public class ContactMapModel implements ContactMapInteractor {

    private ContactMapRepository repo;

    @Inject
    public ContactMapModel(ContactMapRepository repo) {
        this.repo = repo;
    }

    @Override
    public Completable submitContact(String lookupKey, Address currentAddress) {
        return repo.submitContact(lookupKey, currentAddress);
    }

    @Override
    public Single<Address> loadContactAddress(String lookupKey) {
        return repo.loadContactAddress(lookupKey);
    }

    @Nullable
    @Override
    public Single<Address> geodecode(PinPoint pinPoint) {
        return repo.decode(pinPoint.getLat(), pinPoint.getLon())
                .map(pointName -> new AddressBean(pinPoint.getLat(), pinPoint.getLon(), pointName));
    }
}
