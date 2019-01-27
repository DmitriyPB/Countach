package com.testing.android.countach.presentation.allpoints;

import android.Manifest;

import com.testing.android.countach.CountachApp;
import com.testing.android.countach.MainActivity;
import com.testing.android.countach.TestUtils;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.PinPoint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import dagger.android.AndroidInjector;

import static org.mockito.internal.util.MockUtil.isMock;

public class AllPointsFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            CountachApp app = ((CountachApp) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
            app.activityDispatchingAndroidInjector = TestUtils.createFakeMainActivityInjector(buildFragmentAndroidInjectorMap(), () -> allPointsFragment);
        }
    };

    private Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> buildFragmentAndroidInjectorMap() {
        Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap = new HashMap<>();
        fragmentInjectorMap.put(AllPointsFragment.class, new AllPointsFragmentAndroidInjector(() -> mockAllPointsPresenter));
        return fragmentInjectorMap;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);

    private AllPointsFragment allPointsFragment = AllPointsFragment.newInstance();

    @Mock
    private AllPointsPresenter mockAllPointsPresenter;

    private UiDevice device;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        activityRule.launchActivity(null);
    }

    @UiThreadTest
    @Test
    public void testPresenterIsMocked() {
        Assert.assertTrue(isMock(allPointsFragment.presenter));
        Assert.assertSame(allPointsFragment.presenter, mockAllPointsPresenter);
        Assert.assertTrue(activityRule.getActivity().getSupportFragmentManager().getFragments().contains(allPointsFragment));
    }

    @Test
    public void testApplyingPoints() throws Throwable {
        LinkedList<PinPoint> points = new LinkedList<>();
        points.add(new AddressBean(1, 1, "one"));
        points.add(new AddressBean(2, 2, "two"));
        activityRule.runOnUiThread(() -> {
            allPointsFragment.applyPinPoints(points);
        });

        UiObject map = device.findObject(new UiSelector().description("Google Map"));
        int markersCount = map.getChildCount();
        Assert.assertEquals(points.size(), markersCount);
    }

    @UiThreadTest
    @Test
    public void testInitialPointsLoading() {
        Mockito.verify(mockAllPointsPresenter).loadAllPinPoints();
    }

}