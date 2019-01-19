package com.testing.android.countach.presentation.allpoints;

import com.testing.android.countach.data.allpoints.AllPointsRepositoryImpl;
import com.testing.android.countach.domain.allpoints.AllPointsRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface AllPointsModule {
    @Binds
    @AllPointsScope
    AllPointsPresenter providePresenter(AllPointsPresenterImpl presenter);

    @Binds
    @AllPointsScope
    AllPointsRepository provideRepository(AllPointsRepositoryImpl repository);
}
