package com.testing.android.countach;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.testing.android.countach.domain.Contact;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final public class Repository {

    private Context appContext;
    private static final String[] PROJECTION_ALL_CONTACTS = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };

    Repository(Context appContext) {
        this.appContext = appContext;
    }

    public List<Contact> getContactList() {
        try (Cursor cursor = appContext.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION_ALL_CONTACTS,
                null,
                null,
                ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY
        )) {

            if (cursor != null && cursor.getCount() > 0) {
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
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static final String[] PROJECTION_CONTACT_DETAILS = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };
    private static final String SELECTION_CONTACT_DETAILS = ContactsContract.Data.LOOKUP_KEY + " = ?";

    public Contact getContactDetails(@NonNull String lookup) {
        String[] SELECTION_ARGS = {lookup};
        try (Cursor cursor = appContext.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION_CONTACT_DETAILS,
                SELECTION_CONTACT_DETAILS,
                SELECTION_ARGS,
                null
        )) {
            if (cursor != null && cursor.getCount() > 0) {
                int phoneColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
                int lookupColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
                int typeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.MIMETYPE);

                cursor.moveToFirst();

                String lookupKey = null;
                String phoneNumber = null;
                String email = null;
                String name = null;
                do {
                    lookupKey = cursor.getString(lookupColumnIndex);
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
                return new Contact(name, phoneNumber, email, lookupKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
