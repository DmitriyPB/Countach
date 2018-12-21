package com.testing.android.countach;


import com.testing.android.countach.details.ContactDetailsComponent;
import com.testing.android.countach.details.ContactDetailsModule;
import com.testing.android.countach.listing.ContactsListingComponent;
import com.testing.android.countach.listing.ContactsListingModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class
})
public interface AppComponent {
    ContactDetailsComponent plus(ContactDetailsModule contactDetailsModule);

    ContactsListingComponent plus(ContactsListingModule contactsListingModule);
}
