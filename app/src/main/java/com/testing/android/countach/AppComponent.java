package com.testing.android.countach;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class,
        AppModule.class,
        DataModule.class,
        ActivityBuilder.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(CountachApp app);

        Builder appModule(AppModule appModule);

        AppComponent build();
    }

    void inject(CountachApp app);
}
