package com.testing.android.countach.listing;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.AppExecutors;
import com.testing.android.countach.Repository;
import com.testing.android.countach.domain.Contact;

import java.util.List;
import java.util.concurrent.Future;

@InjectViewState
final public class ContactListPresenter extends MvpPresenter<ContactListView> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    private final Repository repo;
    private final AppExecutors executors;
    private Future<?> contactFuture;

    public ContactListPresenter(Repository repo, AppExecutors executors) {
        this.repo = repo;
        this.executors = executors;
    }

    public void loadContacts() {
        contactFuture = executors.worker().submit(() -> {
            final List<Contact> contactList = repo.getContactList();
            executors.ui().execute(() -> {
                getViewState().applyContacts(contactList);
            });
        });
    }

    @Override
    public void onDestroy() {
        if (contactFuture != null) {
            contactFuture.cancel(false);
        }
        super.onDestroy();
    }
}
