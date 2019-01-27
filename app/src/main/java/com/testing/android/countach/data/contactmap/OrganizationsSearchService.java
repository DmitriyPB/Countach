package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.domain.Organization;

import java.util.List;

import androidx.annotation.NonNull;

public interface OrganizationsSearchService {

    @NonNull
    List<Organization> searchForOrganizations(@NonNull String apiKey, @NonNull String query);
}
