package com.testing.android.countach.domain.listing;

import android.support.annotation.Nullable;

import com.testing.android.countach.domain.Contact;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

final public class ContactsListingModel implements ContactsListingInteractor {
    private ContactsListingRepository repo;

    @Inject
    public ContactsListingModel(ContactsListingRepository repo) {
        this.repo = repo;
    }

    @Override
    public Single<List<Contact>> getContactList(@Nullable String likeName) {
        return repo.getContactList(likeName);
    }
}
