package com.testing.android.countach.domain.organizationsearch;

import java.util.List;

import io.reactivex.Single;

public interface OrgsListingRepository {
    Single<List<OrgSearchResult>> loadOrganizationsList(String query);
}
