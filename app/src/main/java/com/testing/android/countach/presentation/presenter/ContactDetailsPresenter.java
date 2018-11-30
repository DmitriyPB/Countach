package com.testing.android.countach.presentation.presenter;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.AppExecutors;
import com.testing.android.countach.Repository;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.view.ContactDetailsView;

@InjectViewState
final public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {

    private static final String TAG = ContactDetailsPresenter.class.getSimpleName();
    private final Repository repo;
    private final AppExecutors executors;

    public ContactDetailsPresenter(Repository repo, AppExecutors executors) {
        this.repo = repo;
        this.executors = executors;
    }

    public void loadContactDetails(@NonNull String lookupKey) {
        executors.worker().execute(() -> {
            Contact contactDetails = repo.getContactDetails(lookupKey);
            executors.ui().execute(() -> {
                getViewState().applyContact(contactDetails);
            });
        });
    }
}
