package com.testing.android.countach.data.contactmap;

import com.testing.android.countach.domain.Organization;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;

public interface OrganizationSearchService {

    List<Organization> searchForOrganizations(@NonNull String apiKey,
                                              @NonNull String text
    ) throws IOException, ParseException;
}
