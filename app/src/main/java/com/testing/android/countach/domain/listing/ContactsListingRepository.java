package com.testing.android.countach.domain.listing;

import androidx.annotation.Nullable;

import com.testing.android.countach.domain.Contact;

import java.util.List;

import io.reactivex.Single;

public interface ContactsListingRepository {
    Single<List<Contact>> getContactList(@Nullable String likeName);
}
