package com.testing.android.countach.domain.details;

import android.support.annotation.NonNull;

import com.testing.android.countach.domain.Contact;

import io.reactivex.Single;

public interface ContactDetailsInteractor {
    Single<Contact> getContactDetails(@NonNull String lookup);
}
