package com.testing.android.countach.presentation.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactListView;

import java.util.LinkedList;
import java.util.List;

@InjectViewState
final public class ContactListPresenter extends MvpPresenter<ContactListView> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };

    public void loadContacts(Context context) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY
        );
        if (cursor == null) return;
        if (cursor.getCount() == 0) {
            cursor.close();
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
        cursor.close();
        getViewState().applyContacts(list);
    }
}
