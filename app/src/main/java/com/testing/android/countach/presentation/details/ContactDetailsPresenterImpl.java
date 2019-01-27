package com.testing.android.countach.presentation.details;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.details.ContactDetailsInteractor;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactDetailsPresenterImpl extends ContactDetailsPresenter {

    private static final String TAG = ContactDetailsPresenterImpl.class.getSimpleName();
    private final ContactDetailsInteractor interactor;
    private Disposable subscriptionContact;

    @Inject
    ContactDetailsPresenterImpl(ContactDetailsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void loadContactDetails(@NonNull String lookupKey) {
        disposeDetailsSubscriptionSafely();
        subscriptionContact = interactor.getContactDetails(lookupKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showLoadingIndicator(true))
                .doAfterTerminate(() -> getViewState().showLoadingIndicator(false))
                .subscribe(this::onGetContactSuccess, this::onGetContactFailure);
    }

    private void disposeDetailsSubscriptionSafely() {
        if (subscriptionContact != null && !subscriptionContact.isDisposed()) {
            subscriptionContact.dispose();
        }
    }

    private void onGetContactFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onGetContactSuccess(Contact contactDetails) {
        getViewState().applyContact(contactDetails);
    }

    @Override
    public void onDestroy() {
        disposeDetailsSubscriptionSafely();
        super.onDestroy();
    }
}
