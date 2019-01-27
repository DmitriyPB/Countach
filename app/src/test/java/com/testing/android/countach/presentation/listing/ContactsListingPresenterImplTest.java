package com.testing.android.countach.presentation.listing;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.listing.ContactsListingInteractor;
import com.testing.android.countach.rxschedulers.SchedulersProvider;
import com.testing.android.countach.rxschedulers.SequentialSchedulersProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContactsListingPresenterImplTest {

    @Mock
    private ContactsListingInteractor mockInteractor;
    @Mock
    private ContactsListingView mockView;

    private ContactsListingPresenterImpl presenter;
    private SchedulersProvider schedulers = new SequentialSchedulersProvider();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new ContactsListingPresenterImpl(mockInteractor, schedulers);
        presenter.attachView(mockView);
    }

    @Test
    public void testInitialViewCallForLoadingContacts() {
        verify(mockView).loadContactsWithPermissionCheck();
    }

    @Test
    public void testLoadContactsInteractorCall() {
        List<Contact> mockedList = Collections.emptyList();

        String query = "spec";
        when(mockInteractor.getContactList(query)).thenReturn(Single.just(mockedList));
        presenter.loadContacts(query);
        verify(mockInteractor).getContactList(query);
    }

    @Test
    public void testLoadContactsIndicators() {
        List<Contact> mockedList = Collections.emptyList();

        when(mockInteractor.getContactList(anyString())).thenReturn(Single.just(mockedList));
        presenter.loadContacts(anyString());

        InOrder order = inOrder(mockView, mockView);
        order.verify(mockView).showLoadingIndicator(true);
        order.verify(mockView).showLoadingIndicator(false);
    }

    @Test
    public void testLoadContactsSuccess() {
        List<Contact> mockedList = Collections.emptyList();

        when(mockInteractor.getContactList(anyString())).thenReturn(Single.just(mockedList));
        presenter.loadContacts(anyString());
        verify(mockView).applyContacts(mockedList);
    }

    @Test
    public void testLoadContactsFailure() {
        List<Contact> mockedList = Collections.emptyList();

        when(mockInteractor.getContactList(anyString())).thenReturn(Single.error(Exception::new));
        presenter.loadContacts(anyString());
        verify(mockView, never()).applyContacts(mockedList);
    }
}