package com.testing.android.countach.domain.contactmap;

import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.Organization;

import java.util.List;

import io.reactivex.Single;

public interface ContactMapRepository {
    Single<Integer> submitContact(String lookupKey, Address currentAddress, List<Organization> currentOrganizationsList);

    Single<Address> loadContactAddress(String lookupKey);

    Single<String> decode(double lat, double lon);

    Single<List<Organization>> searchForOrganizations(String addressName);
}
