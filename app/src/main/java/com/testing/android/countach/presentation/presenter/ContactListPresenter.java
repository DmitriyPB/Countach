package com.testing.android.countach.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.AppExecutors;
import com.testing.android.countach.Repository;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactListView;

import java.util.List;

@InjectViewState
final public class ContactListPresenter extends MvpPresenter<ContactListView> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    private final Repository repo;
    private AppExecutors executors;

    public ContactListPresenter(Repository repo, AppExecutors executors) {
        this.repo = repo;
        this.executors = executors;
    }

    public void loadContacts() {
        executors.worker().execute(() -> {
            final List<Contact> contactList = repo.getContactList();
            executors.ui().execute(() -> {
                getViewState().applyContacts(contactList);
            });
        });
    }
}
