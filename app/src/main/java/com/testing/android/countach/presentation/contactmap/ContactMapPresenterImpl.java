package com.testing.android.countach.presentation.contactmap;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.domain.PinPoint;
import com.testing.android.countach.domain.contactmap.ContactMapInteractor;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class ContactMapPresenterImpl extends ContactMapPresenter {

    private ContactMapInteractor interactor;
    private Disposable subscriptionLoading;
    private Disposable subscriptionSaving;
    private Disposable subscriptionGeodecoding;

    @Inject
    ContactMapPresenterImpl(ContactMapInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void loadPinPointForContact(String lookupKey) {
        disposeLoadingSubscriptionSafely();
        subscriptionLoading = interactor.loadContactAddress(lookupKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadPinPointSuccess, this::onLoadPinPointFailure);
    }

    @Override
    public void submitPinPoint(String lookupKey, Address address) {
        disposeSavingSubscriptionSafely();
        subscriptionSaving = interactor.submitContact(lookupKey, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSubmitPinPointSuccess, this::onSubmitPinPointFailure);
    }

    @Override
    void submitGeodecoding(PinPoint pinPoint) {
        disposeGeodecodingSubscriptionSafely();
        subscriptionGeodecoding = interactor.geodecode(pinPoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showGeodecodingIndicator(true))
                .doAfterTerminate(() -> getViewState().showGeodecodingIndicator(false))
                .subscribe(this::onGeodecodingSuccess, this::onGeodecodingFailure);
    }

    private void disposeGeodecodingSubscriptionSafely() {
        if (subscriptionGeodecoding != null && !subscriptionGeodecoding.isDisposed()) {
            subscriptionGeodecoding.dispose();
        }
    }

    private void onGeodecodingFailure(Throwable throwable) {
        throwable.printStackTrace();
        getViewState().onGeodecodingFinished(null);
    }

    private void onGeodecodingSuccess(Address address) {
        getViewState().onGeodecodingFinished(address);
    }

    private void disposeSavingSubscriptionSafely() {
        if (subscriptionSaving != null && !subscriptionSaving.isDisposed()) {
            subscriptionSaving.dispose();
        }
    }

    private void onSubmitPinPointFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onSubmitPinPointSuccess() {
        getViewState().onSubmitSuccess();
    }

    private void disposeLoadingSubscriptionSafely() {
        if (subscriptionLoading != null && !subscriptionLoading.isDisposed()) {
            subscriptionLoading.dispose();
        }
    }

    private void onLoadPinPointFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onLoadPinPointSuccess(Address address) {
        getViewState().applyContactPinPoint(address);
    }

    @Override
    public void onDestroy() {
        disposeLoadingSubscriptionSafely();
        disposeSavingSubscriptionSafely();
        disposeGeodecodingSubscriptionSafely();
        super.onDestroy();
    }
}
