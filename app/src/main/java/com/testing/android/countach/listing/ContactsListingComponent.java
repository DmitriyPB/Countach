package com.testing.android.countach.listing;

import dagger.Subcomponent;

@ContactsListingScope
@Subcomponent(modules = {ContactsListingModule.class})
public interface ContactsListingComponent {
    void inject(ContactsListingFragment target);
}
