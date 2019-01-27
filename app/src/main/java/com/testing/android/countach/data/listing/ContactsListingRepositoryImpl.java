package com.testing.android.countach.data.listing;

import android.provider.ContactsContract;

import com.testing.android.countach.data.ContactsDao;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.listing.ContactsListingRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.Single;


final public class ContactsListingRepositoryImpl implements ContactsListingRepository {
    private ContactsDao contactsDao;

    @Inject
    ContactsListingRepositoryImpl(ContactsDao contactsDao) {
        this.contactsDao = contactsDao;
    }

    public Single<List<Contact>> getContactList(@Nullable String likeName) {
        return Single.fromCallable(() -> {
            String SELECTION_CONTACT_LIST = null;
            String[] SELECTION_ARGS = null;
            if (likeName != null && !likeName.isEmpty()) {
                SELECTION_CONTACT_LIST = ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY + " LIKE ?";
                SELECTION_ARGS = new String[]{"%" + likeName + "%"};
            }
            return contactsDao.queryContacts(SELECTION_CONTACT_LIST, SELECTION_ARGS, ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY);
        });
    }


}
