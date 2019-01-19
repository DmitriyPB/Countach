package com.testing.android.countach.data;

import com.testing.android.countach.domain.Contact;

import java.util.List;

public interface ContactsDao {
    List<Contact> queryContacts(String selection, String[] selectionArgs, String sortBy);
}
