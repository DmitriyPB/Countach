package com.testing.android.countach.listing;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.Repository;
import com.testing.android.countach.domain.Contact;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactListPresenter extends MvpPresenter<ContactListView> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    private final Repository repo;
    private Disposable subscriptionContactList;

    ContactListPresenter(Repository repo) {
        this.repo = repo;
        getViewState().loadContactsWithPermissionCheck();
    }

    void loadContacts(@Nullable String query) {
        subscriptionContactList = Single.fromCallable(() -> repo.getContactList(query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showLoadingIndicator(true))
                .doAfterTerminate(() -> getViewState().showLoadingIndicator(false))
                .subscribe(this::onGetContactsSuccess, this::onGetContactsFailure);
    }

    private void onGetContactsFailure(Throwable error) {
        error.printStackTrace();
    }

    private void onGetContactsSuccess(List<Contact> contacts) {
        getViewState().applyContacts(contacts);
    }

    @Override
    public void onDestroy() {
        if (subscriptionContactList != null) {
            subscriptionContactList.dispose();
        }
        super.onDestroy();
    }
}
