package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.Organization;
import com.testing.android.countach.domain.PinPoint;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

final public class ContactMapModel implements ContactMapInteractor {

    private final List<Organization> currentOrganizationsList = new LinkedList<>();
    private Address currentAddress;
    private Disposable orgSubscription;
    private String lookupKey;
    private ContactMapRepository repo;

    @Inject
    public ContactMapModel(ContactMapRepository repo) {
        this.repo = repo;
    }

    @Override
    public Single<Integer> submitContact() {
        return repo.submitContact(lookupKey, currentAddress, currentOrganizationsList);
    }

    @Override
    public Flowable<Address> loadContactAddress(String lookupKey) {
        this.lookupKey = lookupKey;
        return repo.loadContactAddress(lookupKey);
    }

    @Nullable
    @Override
    public Single<Address> geodecode(PinPoint pinPoint) {
        return decodeSync(pinPoint);
    }

    private void searchForOrganizationsAsync(Address address) {
        currentOrganizationsList.clear();
        disposeOrgSubscription();
        orgSubscription = repo.searchForOrganizations(address.getAddressName())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onOrgSearchSuccess, this::onOrgSearchFailure);
    }

    private void onOrgSearchFailure(Throwable throwable) {
        throwable.printStackTrace();
        disposeOrgSubscription();
    }

    private void onOrgSearchSuccess(List<Organization> orgs) {
        if (orgs != null) {
            currentOrganizationsList.addAll(orgs);
        }
        disposeOrgSubscription();
    }

    private void disposeOrgSubscription() {
        if (orgSubscription != null && !orgSubscription.isDisposed()) {
            orgSubscription.dispose();
        }
    }

    private Single<Address> decodeSync(PinPoint point) {
        return repo.decode(point.getLat(), point.getLon())
                .map(pointName -> new AddressBean(point.getLat(), point.getLon(), pointName))
                .doOnEvent(this::cachePoint)
                .cast(AddressBean.class);
    }

    private void cachePoint(AddressBean address, Throwable throwable) {
        if (!address.getAddressName().isEmpty()) {
            searchForOrganizationsAsync(address);
            currentAddress = address;
        } else {
            currentAddress = null;
        }
    }
}
