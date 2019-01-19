package com.testing.android.countach.presentation.allpoints;

import dagger.Subcomponent;

@AllPointsScope
@Subcomponent(modules = {AllPointsModule.class})
public interface AllPointsComponent {
    void inject(AllPointsFragment target);
}
