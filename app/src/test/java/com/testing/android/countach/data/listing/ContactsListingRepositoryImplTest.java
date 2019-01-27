package com.testing.android.countach.data.listing;

import com.testing.android.countach.data.ContactsDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

public class ContactsListingRepositoryImplTest {

    @Mock
    private ContactsDao contactsDao;
    private ContactsListingRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(contactsDao.queryContacts(any(), any(), any())).thenReturn(Collections.emptyList());
        repository = new ContactsListingRepositoryImpl(contactsDao);
    }

    @Test
    public void getContactListNormalQuery() {
        String query = "normal";
        repository.getContactList(query).subscribe();
        Mockito.verify(contactsDao).queryContacts(anyString(), eq(new String[]{"%" + query + "%"}), anyString());
    }

    @Test
    public void getContactListNullQuery() {
        repository.getContactList(null).subscribe();
        Mockito.verify(contactsDao).queryContacts(isNull(), isNull(), anyString());
    }

    @Test
    public void getContactListEmptyQuery() {
        repository.getContactList("").subscribe();
        Mockito.verify(contactsDao).queryContacts(isNull(), isNull(), anyString());
    }
}