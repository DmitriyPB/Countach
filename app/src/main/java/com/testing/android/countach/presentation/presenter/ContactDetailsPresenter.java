package com.testing.android.countach.presentation.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactDetailsView;
import com.testing.android.countach.ui.fragment.ContactDetailFragment;

@InjectViewState
final public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {

    private static final String TAG = ContactDetailsPresenter.class.getSimpleName();
    private static final int CONTACT_DETAILS_LOADER = 1;
    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };
    private static final String SELECTION = ContactsContract.Data.LOOKUP_KEY + " = ?";
    private String[] SELECTION_ARGS = {""};


    public void loadContactDetails(Bundle arguments, Context context) {
        if (arguments != null) {
            SELECTION_ARGS[0] = arguments.getString(ContactDetailFragment.LOOKUP_KEY_KEY);
        }
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                SELECTION,
                SELECTION_ARGS,
                null
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
        getViewState().applyContact(new Contact(name, phoneNumber, email, lookupKey));
    }
}
