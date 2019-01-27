package com.testing.android.countach.domain.details;

import androidx.annotation.NonNull;

import com.testing.android.countach.domain.Contact;

import io.reactivex.Single;

public interface ContactDetailsRepository {
    Single<Contact> getContactDetails(@NonNull String lookup);
}
