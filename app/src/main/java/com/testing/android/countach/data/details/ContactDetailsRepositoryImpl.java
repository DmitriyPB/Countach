package com.testing.android.countach.data.details;

import android.provider.ContactsContract;

import com.testing.android.countach.data.ContactsDao;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.details.ContactDetailsRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Single;

final public class ContactDetailsRepositoryImpl implements ContactDetailsRepository {

    private static final String SELECTION_CONTACT_DETAILS = ContactsContract.Data.LOOKUP_KEY + " = ?";
    private ContactsDao contactsDao;

    @Inject
    ContactDetailsRepositoryImpl(ContactsDao contactsDao) {
        this.contactsDao = contactsDao;
    }

    public Single<Contact> getContactDetails(@NonNull String lookup) {
        return Single.fromCallable(() -> {
            String[] SELECTION_ARGS = {lookup};
            List<Contact> contacts = contactsDao.queryContacts(SELECTION_CONTACT_DETAILS, SELECTION_ARGS, null);
            if (!contacts.isEmpty()) {
                return contacts.get(0);
            }
            throw new NullPointerException("not found");
        });
    }
}
