package com.testing.android.countach.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.testing.android.countach.domain.Contact;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

final public class ContactsDaoImpl implements ContactsDao {

    private static final String[] PROJECTION_ALL_CONTACTS = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };

    private final Context appContext;

    @Inject
    public ContactsDaoImpl(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public List<Contact> queryContacts(String selection, String[] selectionArgs, String sortBy) {
        try (Cursor cursor = appContext.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION_ALL_CONTACTS,
                selection,
                selectionArgs,
                sortBy
        )) {
            return extractContacts(cursor);
        }
    }

    private List<Contact> extractContacts(Cursor cursor) {
        List<Contact> container = new LinkedList<>();
        if (cursor != null && cursor.getCount() > 0) {

            int phoneColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
            int lookupColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
            int typeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.MIMETYPE);

            Constructor last = new Constructor();
            cursor.moveToFirst();
            do {
                String newLookup = cursor.getString(lookupColumnIndex);
                if (!last.isSameEntry(newLookup)) {
                    if (last.isReady()) {
                        container.add(last.toContact());
                    }
                    last = new Constructor(newLookup);
                }
                if (typeColumnIndex != -1) {
                    String mimeType = cursor.getString(typeColumnIndex);
                    if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        last.phoneNumber = cursor.getString(phoneColumnIndex);
                    } else if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                        last.email = cursor.getString(emailColumnIndex);
                    } else if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                        last.name = cursor.getString(nameColumnIndex);
                    }
                }
            } while (cursor.moveToNext());
            if (last.isReady()) {
                container.add(last.toContact());
            }
        }
        return container;
    }

    private class Constructor {
        String lookupKey = null;
        String phoneNumber = null;
        String email = null;
        String name = null;

        Constructor(String lookupKey) {
            this.lookupKey = lookupKey;
        }

        Constructor() {
        }

        @Override
        public String toString() {
            return "Constructor{" +
                    "lookupKey='" + lookupKey + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        boolean isReady() {
            return lookupKey != null && name != null;
        }

        boolean isSameEntry(String lookup) {
            return lookupKey != null && lookupKey.equals(lookup);
        }

        ContactBean toContact() {
            return new ContactBean(name, phoneNumber, email, lookupKey);
        }
    }
}
