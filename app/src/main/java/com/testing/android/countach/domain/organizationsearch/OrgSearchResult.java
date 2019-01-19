package com.testing.android.countach.domain.organizationsearch;

import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.Contact;

import java.util.List;

public interface OrgSearchResult {
    Address getOrg();

    List<Contact> getNearestContacts();
}
