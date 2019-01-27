package com.testing.android.countach;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ProgressBar;

import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.DispatchingAndroidInjector_Factory;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static java.util.Collections.emptyMap;

public class TestUtils {
    private TestUtils() {
    }

    public static DispatchingAndroidInjector<Activity> createFakeMainActivityInjector(
            final Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap, final Provider<Fragment> initialFragmentProvider
    ) {

        Map<Class<?>, Provider<AndroidInjector.Factory<?>>> fragmentMap = new HashMap<>(1);

        Provider<AndroidInjector.Factory<?>> fragmentInjectorFactoryProvider = new Provider<AndroidInjector.Factory<?>>() {
            @Override
            public AndroidInjector.Factory<?> get() {
                return new AndroidInjector.Factory<Fragment>() {
                    @Override
                    public AndroidInjector<Fragment> create(Fragment instance) {
                        return (AndroidInjector<Fragment>) fragmentInjectorMap.get(instance.getClass());
                    }
                };
            }
        };
        for (Class<?> fragmentClass : fragmentInjectorMap.keySet()) {
            fragmentMap.put(fragmentClass, fragmentInjectorFactoryProvider);
        }

        Provider<AndroidInjector.Factory<?>> activityInjectorFactoryProvider = new Provider<AndroidInjector.Factory<?>>() {
            @Override
            public AndroidInjector.Factory<?> get() {
                return (AndroidInjector.Factory<Activity>) new AndroidInjector.Factory<Activity>() {
                    @Override
                    public AndroidInjector<Activity> create(Activity instance) {
                        return new AndroidInjector<Activity>() {
                            @Override
                            public void inject(Activity instance1) {
                                if (instance1 instanceof MainActivity) {
                                    MainActivity mainActivity = (MainActivity) instance1;
                                    mainActivity.initialFragmentProvider = initialFragmentProvider;
                                    mainActivity.fragmentDispatchingAndroidInjector =
                                            DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(fragmentMap, emptyMap());
                                }
                            }
                        };
                    }
                };
            }
        };
        Map<Class<?>, Provider<AndroidInjector.Factory<?>>> activityMap = new HashMap<>(1);
        activityMap.put(MainActivity.class, activityInjectorFactoryProvider);

        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(activityMap, emptyMap());
    }

    public static ViewAction replaceProgressBarDrawable() {
        return actionWithAssertions(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ProgressBar.class);
            }

            @Override
            public String getDescription() {
                return "replace the ProgressBar drawable";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                // Replace the indeterminate drawable with a static red ColorDrawable
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setIndeterminateDrawable(new ColorDrawable(0xffff0000));
                uiController.loopMainThreadUntilIdle();
            }
        });
    }
}
