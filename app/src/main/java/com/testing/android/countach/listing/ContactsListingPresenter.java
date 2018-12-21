package com.testing.android.countach.listing;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.IRepository;
import com.testing.android.countach.domain.Contact;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactsListingPresenter extends MvpPresenter<ContactsListingView> {

    private static final String TAG = ContactsListingPresenter.class.getSimpleName();

    private final IRepository repo;
    private Disposable subscriptionContactList;

    ContactsListingPresenter(IRepository repo) {
        this.repo = repo;
        getViewState().loadContactsWithPermissionCheck();
    }

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
