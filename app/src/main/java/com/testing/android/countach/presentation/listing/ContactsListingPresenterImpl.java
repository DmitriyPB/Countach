package com.testing.android.countach.presentation.listing;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.listing.ContactsListingInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactsListingPresenterImpl extends ContactsListingPresenter {

    private static final String TAG = ContactsListingPresenterImpl.class.getSimpleName();

    private final ContactsListingInteractor repo;
    private Disposable subscriptionContactList;

    @Inject
    ContactsListingPresenterImpl(ContactsListingInteractor repo) {
        this.repo = repo;
        getViewState().loadContactsWithPermissionCheck();
    }

    @Override
    void loadContacts(@Nullable String query) {
        disposeContactsSubscriptionSafely();
        subscriptionContactList = repo.getContactList(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showLoadingIndicator(true))
                .doAfterTerminate(() -> getViewState().showLoadingIndicator(false))
                .subscribe(this::onGetContactsSuccess, this::onGetContactsFailure);
    }

    private void disposeContactsSubscriptionSafely() {
        if (subscriptionContactList != null && !subscriptionContactList.isDisposed()) {
            subscriptionContactList.dispose();
        }
    }

    private void onGetContactsFailure(Throwable error) {
        error.printStackTrace();
    }

    private void onGetContactsSuccess(List<Contact> contacts) {
        getViewState().applyContacts(contacts);
    }

    @Override
    public void onDestroy() {
        disposeContactsSubscriptionSafely();
        super.onDestroy();
    }
}
