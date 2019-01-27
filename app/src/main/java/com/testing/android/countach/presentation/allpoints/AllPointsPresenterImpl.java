package com.testing.android.countach.presentation.allpoints;

import com.arellomobile.mvp.InjectViewState;
import com.testing.android.countach.domain.PinPoint;
import com.testing.android.countach.domain.allpoints.AllPointsRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
final public class AllPointsPresenterImpl extends AllPointsPresenter {

    private AllPointsRepository repo;
    private Disposable subscriptionLoading;

    @Inject
    AllPointsPresenterImpl(AllPointsRepository repo) {
        this.repo = repo;
    }

    @Override
    public void loadAllPinPoints() {
        disposeLoadingSubscriptionSafely();
        subscriptionLoading = repo.loadPoints()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadSuccess, this::onLoadFailure);
    }

    private void disposeLoadingSubscriptionSafely() {
        if (subscriptionLoading != null && !subscriptionLoading.isDisposed()) {
            subscriptionLoading.dispose();
        }
    }

    private void onLoadFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onLoadSuccess(List<? extends PinPoint> points) {
        getViewState().applyPinPoints(points);
    }

    @Override
    public void onDestroy() {
        disposeLoadingSubscriptionSafely();
        super.onDestroy();
    }
}
