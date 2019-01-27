package com.testing.android.countach.presentation.organizationsearch;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;
import com.testing.android.countach.domain.organizationsearch.OrgsListingRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class OrgsListingPresenterImpl extends OrgsListingPresenter {

    private static final String TAG = OrgsListingPresenterImpl.class.getSimpleName();

    private final OrgsListingRepository repo;
    private Disposable subscriptionLoading;

    @Inject
    OrgsListingPresenterImpl(OrgsListingRepository repo) {
        this.repo = repo;
        loadOrganizationByQuery(null);
    }

    private void disposeLoadingSubscriptionSafely() {
        if (subscriptionLoading != null && !subscriptionLoading.isDisposed()) {
            subscriptionLoading.dispose();
        }
    }

    private void onLoadFailure(Throwable error) {
        error.printStackTrace();
    }

    private void onLoadSuccess(List<OrgSearchResult> orgs) {
        getViewState().applyResult(orgs);
    }

    @Override
    public void onDestroy() {
        disposeLoadingSubscriptionSafely();
        super.onDestroy();
    }

    @Override
    void loadOrganizationByQuery(@Nullable String query) {
        disposeLoadingSubscriptionSafely();
        subscriptionLoading = repo.loadOrganizationsList(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().showLoadingIndicator(true))
                .doAfterTerminate(() -> getViewState().showLoadingIndicator(false))
                .subscribe(this::onLoadSuccess, this::onLoadFailure);
    }
}
