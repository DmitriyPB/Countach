package com.testing.android.countach.data.allpoints;

import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.PinPoint;
import com.testing.android.countach.domain.allpoints.AllPointsRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

final public class AllPointsRepositoryImpl implements AllPointsRepository {
    private ContactExtraDao dao;

    @Inject
    AllPointsRepositoryImpl(ContactExtraDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<List<? extends PinPoint>> loadPoints() {
        return Single.fromCallable(() -> {
            List<AddressBean> points = dao.loadAllContactPoints();
            if (points != null) {
                return points;
            }
            return Collections.emptyList();
        });
    }
}
