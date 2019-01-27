package com.testing.android.countach.data;

import com.testing.android.countach.domain.Contact;

import java.util.List;

import androidx.annotation.Nullable;

public interface ContactsDao {
    List<Contact> queryContacts(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortBy);
}
