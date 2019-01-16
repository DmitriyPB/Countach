package com.testing.android.countach.presentation.details;

import dagger.Subcomponent;

@ContactDetailsScope
@Subcomponent(modules = {ContactDetailsModule.class})
public interface ContactDetailsComponent {
    void inject(ContactDetailsFragment target);
}
