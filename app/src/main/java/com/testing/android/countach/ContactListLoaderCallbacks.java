package com.testing.android.countach;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.testing.android.countach.data.Contact;

import java.util.LinkedList;
import java.util.List;

public class ContactListLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ContactListLoaderCallbacks.class.getSimpleName();
    private ContactListFragment fragment;

    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };

    public ContactListLoaderCallbacks(ContactListFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(
                fragment.requireContext(),
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 0) {
            return;
        }

        int phoneColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
        int lookupColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
        int typeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.MIMETYPE);

        cursor.moveToFirst();

        String lookupKey = "";
        String phoneNumber = null;
        String email = null;
        String name = null;
        List<Contact> list = new LinkedList<>();
        do {
            String currentLookupKey = cursor.getString(lookupColumnIndex);
            if (!lookupKey.equals(currentLookupKey)) {
                if (!lookupKey.isEmpty()) {
                    if (name != null) {
                        list.add(new Contact(name, phoneNumber, email, lookupKey));
                    }
                }
                phoneNumber = null;
                email = null;
                name = null;
                lookupKey = currentLookupKey;
            }

            if (typeColumnIndex != -1) {
                String mimeType = cursor.getString(typeColumnIndex);
                if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    phoneNumber = cursor.getString(phoneColumnIndex);
                } else if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    email = cursor.getString(emailColumnIndex);
                } else if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    name = cursor.getString(nameColumnIndex);
                }
            }
        } while (cursor.moveToNext());
        fragment.applyContacts(list);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
    }
}
