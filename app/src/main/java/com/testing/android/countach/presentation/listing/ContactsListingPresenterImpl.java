package com.testing.android.countach.presentation.listing;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.listing.ContactsListingInteractor;
import com.testing.android.countach.rxschedulers.SchedulersProvider;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import io.reactivex.disposables.Disposable;

@InjectViewState
final public class ContactsListingPresenterImpl extends ContactsListingPresenter {

    private static final String TAG = ContactsListingPresenterImpl.class.getSimpleName();

    private final ContactsListingInteractor interactor;
    private SchedulersProvider schedulers;
    private Disposable subscriptionContactList;

    @Inject
    ContactsListingPresenterImpl(ContactsListingInteractor interactor, SchedulersProvider schedulersProvider) {
        this.interactor = interactor;
        this.schedulers = schedulersProvider;
        getViewState().loadContactsWithPermissionCheck();
    }

    @Override
    void loadContacts(@Nullable String query) {
        disposeContactsSubscriptionSafely();
        subscriptionContactList = interactor.getContactList(query)
                .subscribeOn(schedulers.ioScheduler())
                .observeOn(schedulers.uiScheduler())
                .doOnSubscribe(onSubscribe -> getViewState().showLoadingIndicator(true))
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
