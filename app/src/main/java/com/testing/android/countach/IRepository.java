package com.testing.android.countach;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testing.android.countach.domain.Contact;

import java.util.List;

import io.reactivex.Single;

public interface IRepository {
    Single<List<Contact>> getContactList(@Nullable String likeName);

    Single<Contact> getContactDetails(@NonNull String lookup);
}
