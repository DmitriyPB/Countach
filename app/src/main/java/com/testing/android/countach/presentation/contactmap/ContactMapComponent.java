package com.testing.android.countach.presentation.contactmap;

import dagger.Subcomponent;

@ContactMapScope
@Subcomponent(modules = {ContactMapModule.class})
public interface ContactMapComponent {
    void inject(ContactMapFragment target);
}
