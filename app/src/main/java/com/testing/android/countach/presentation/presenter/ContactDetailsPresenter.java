package com.testing.android.countach.presentation.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactDetailsView;

@InjectViewState
public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ContactDetailsPresenter.class.getSimpleName();
    private static final int CONTACT_DETAILS_LOADER = 1;
    public static final String LOOKUP_KEY_KEY = "lookup_key_key";
    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };
    private static final String SELECTION = ContactsContract.Data.LOOKUP_KEY + " = ?";
    private String[] SELECTION_ARGS = {""};
    private LoaderProvider loaderProvider;

    public ContactDetailsPresenter(LoaderProvider loaderProvider) {
        this.loaderProvider = loaderProvider;
    }

    public void loadContactDetails(Bundle arguments, LoaderManager loaderManager) {
        loaderManager.initLoader(CONTACT_DETAILS_LOADER, arguments, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (bundle != null) {
            SELECTION_ARGS[0] = bundle.getString(LOOKUP_KEY_KEY);
        }
        return loaderProvider.provideLoader(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                SELECTION,
                SELECTION_ARGS,
                null
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

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }
}
