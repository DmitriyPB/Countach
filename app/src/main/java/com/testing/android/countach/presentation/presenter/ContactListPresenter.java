package com.testing.android.countach.presentation.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactListView;

import java.util.LinkedList;
import java.util.List;

import static com.testing.android.countach.ui.fragment.ContactListFragment.CONTACTS_LOADER;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };

    private LoaderProvider loaderProvider;

    public ContactListPresenter(LoaderProvider loaderProvider) {
        this.loaderProvider = loaderProvider;
    }

    public void loadContacts(LoaderManager loaderManager) {
        loaderManager.initLoader(CONTACTS_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return loaderProvider.provideLoader(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY
        );
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
        getViewState().applyContacts(list);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }
}
