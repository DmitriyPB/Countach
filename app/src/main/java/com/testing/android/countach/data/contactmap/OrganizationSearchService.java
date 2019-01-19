package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.domain.Organization;

import java.util.List;

import io.reactivex.Single;

public interface OrganizationSearchService {

    Single<List<Organization>> searchForOrganizations(String apiKey,
                                                      String text
    );
}
