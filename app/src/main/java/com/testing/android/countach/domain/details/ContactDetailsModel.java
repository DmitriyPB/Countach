package com.testing.android.countach.domain.details;

import androidx.annotation.NonNull;

import com.testing.android.countach.domain.Contact;

import javax.inject.Inject;

import io.reactivex.Single;

final public class ContactDetailsModel implements ContactDetailsInteractor {
    private ContactDetailsRepository repo;

    @Inject
    ContactDetailsModel(ContactDetailsRepository repo) {
        this.repo = repo;
    }

    @Override
    public Single<Contact> getContactDetails(@NonNull String lookup) {
        return repo.getContactDetails(lookup);
    }
}
