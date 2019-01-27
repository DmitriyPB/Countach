package com.testing.android.countach.presentation.organizationsearch;

import dagger.Subcomponent;

@OrgsListingScope
@Subcomponent(modules = {OrgsListingModule.class})
public interface OrgsListingComponent {
    void inject(OrgsListingFragment target);
}
