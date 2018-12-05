package com.testing.android.countach.details;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.AppExecutors;
import com.testing.android.countach.Repository;
import com.testing.android.countach.domain.Contact;

import java.util.concurrent.Future;

@InjectViewState
final public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {

    private static final String TAG = ContactDetailsPresenter.class.getSimpleName();
    private final Repository repo;
    private final AppExecutors executors;
    private Future<?> contactListFuture;

    public ContactDetailsPresenter(Repository repo, AppExecutors executors) {
        this.repo = repo;
        this.executors = executors;
    }

    public void loadContactDetails(@NonNull String lookupKey) {
        contactListFuture = executors.worker().submit(() -> {
            Contact contactDetails = repo.getContactDetails(lookupKey);
            executors.ui().execute(() -> {
                getViewState().applyContact(contactDetails);
            });
        });
    }

    @Override
    public void onDestroy() {
        if (contactListFuture != null) {
            contactListFuture.cancel(false);
        }
        super.onDestroy();
    }
}
