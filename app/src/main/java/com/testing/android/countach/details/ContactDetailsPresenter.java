package com.testing.android.countach.details;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.testing.android.countach.Repository;
import com.testing.android.countach.domain.Contact;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {

    private static final String TAG = ContactDetailsPresenter.class.getSimpleName();
    private final Repository repo;
    private Disposable subscriptionContact;

    ContactDetailsPresenter(Repository repo) {
        this.repo = repo;
    }

    void loadContactDetails(@NonNull String lookupKey) {
        if (subscriptionContact != null) {
            subscriptionContact.dispose();
        }
        subscriptionContact = repo.getContactDetails(lookupKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showLoadingIndicator(true))
                .doAfterTerminate(() -> getViewState().showLoadingIndicator(false))
                .subscribe(this::onGetContactSuccess, this::onGetContactFailure);
    }

    private void onGetContactFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onGetContactSuccess(Contact contactDetails) {
        getViewState().applyContact(contactDetails);
    }

    @Override
    public void onDestroy() {
        if (subscriptionContact != null && !subscriptionContact.isDisposed()) {
            subscriptionContact.dispose();
        }
        super.onDestroy();
    }
}
