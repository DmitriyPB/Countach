package com.testing.android.countach.domain.organizationsearch;

import java.util.List;

import io.reactivex.Observable;

public interface OrgsListingRepository {
    Observable<List<OrgSearchResult>> loadOrganizationsList(String query);
}
