package com.testing.android.countach.data.listing;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.listing.ContactsListingRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;


final public class ContactsListingRepositoryImpl implements ContactsListingRepository {
    private Context appContext;
    private static final String[] PROJECTION_ALL_CONTACTS = {
            ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
            ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };
    @Inject
    public ContactsListingRepositoryImpl(Context appContext) {
        this.appContext = appContext;
    }

    public Single<List<Contact>> getContactList(@Nullable String likeName) {
        return Single.fromCallable(() -> {
            String SELECTION_CONTACT_LIST = null;
            String[] SELECTION_ARGS = null;
            if (likeName != null && !likeName.isEmpty()) {
                SELECTION_CONTACT_LIST = ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY + " LIKE ?";
                SELECTION_ARGS = new String[]{"%" + likeName + "%"};
            }
            try (Cursor cursor = appContext.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    PROJECTION_ALL_CONTACTS,
                    SELECTION_CONTACT_LIST,
                    SELECTION_ARGS,
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
        });
    }
}
