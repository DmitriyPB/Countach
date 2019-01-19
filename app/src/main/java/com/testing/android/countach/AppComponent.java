package com.testing.android.countach;


import com.testing.android.countach.presentation.allpoints.AllPointsComponent;
import com.testing.android.countach.presentation.details.ContactDetailsComponent;
import com.testing.android.countach.presentation.listing.ContactsListingComponent;
import com.testing.android.countach.presentation.contactmap.ContactMapComponent;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingComponent;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class
})
public interface AppComponent {
    ContactDetailsComponent plusContactDetailsComponent();

    ContactsListingComponent plusContactsListingComponent();

    ContactMapComponent plusContactMapComponent();

    AllPointsComponent plusAllPointsComponent();

    OrgsListingComponent plusOrgsListingComponent();
}
