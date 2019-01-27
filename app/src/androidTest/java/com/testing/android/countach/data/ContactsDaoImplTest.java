package com.testing.android.countach.data;

import android.database.MatrixCursor;
import android.provider.ContactsContract;

import com.testing.android.countach.domain.Contact;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ContactsDaoImplTest {

    private MatrixCursor cursor;

    @Before
    public void setUp() throws Exception {
        cursor = new MatrixCursor(
                new String[]{
                        ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY,
                        ContactsContract.CommonDataKinds.Contactables.MIMETYPE,
                        ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME_PRIMARY,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Email.ADDRESS
                }
        );
    }

    @Test
    public void testFullContactCollects() {
        String email = "email";
        String phone = "7777";
        String name = "name";
        String lookup = "look";
        cursor.addRow(new Object[]{lookup, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, name, null, null});
        cursor.addRow(new Object[]{lookup, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, null, phone, phone});
        cursor.addRow(new Object[]{lookup, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, null, email, email});
        List<Contact> contacts = ContactsDaoImpl.extractContacts(cursor);
        Assert.assertEquals(1, contacts.size());

        Contact contact = contacts.get(0);

        Assert.assertEquals(lookup, contact.getLookup());
        Assert.assertEquals(name, contact.getName());
        Assert.assertEquals(phone, contact.getPhoneNumber());
        Assert.assertEquals(email, contact.getEmail());
    }

    @Test
    public void testOnlyNamedContactsCollected() {
        String email = "email";
        String phone = "7777";
        String lookup = "look";
        cursor.addRow(new Object[]{lookup, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, null, phone, phone});
        cursor.addRow(new Object[]{lookup, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, null, email, email});
        List<Contact> contacts = ContactsDaoImpl.extractContacts(cursor);
        Assert.assertEquals(0, contacts.size());
    }
}