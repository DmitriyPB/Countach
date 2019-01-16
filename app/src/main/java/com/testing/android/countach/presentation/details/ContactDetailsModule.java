package com.testing.android.countach.presentation.details;

import com.testing.android.countach.data.details.ContactDetailsRepositoryImpl;
import com.testing.android.countach.domain.details.ContactDetailsInteractor;
import com.testing.android.countach.domain.details.ContactDetailsModel;
import com.testing.android.countach.domain.details.ContactDetailsRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface ContactDetailsModule {

    @Binds
    @ContactDetailsScope
    ContactDetailsPresenter providePresenter(ContactDetailsPresenterImpl presenter);

    @Binds
    @ContactDetailsScope
    ContactDetailsInteractor provideInteractor(ContactDetailsModel interactor);

    @Binds
    @ContactDetailsScope
    ContactDetailsRepository provideRepository(ContactDetailsRepositoryImpl repository);
}

