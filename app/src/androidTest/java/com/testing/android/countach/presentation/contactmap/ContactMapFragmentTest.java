package com.testing.android.countach.presentation.contactmap;

import android.Manifest;

import com.testing.android.countach.CountachApp;
import com.testing.android.countach.MainActivity;
import com.testing.android.countach.R;
import com.testing.android.countach.TestUtils;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.PinPoint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import dagger.android.AndroidInjector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.MockUtil.isMock;

public class ContactMapFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            CountachApp app = ((CountachApp) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
            app.activityDispatchingAndroidInjector = TestUtils.createFakeMainActivityInjector(buildFragmentAndroidInjectorMap(), () -> contactMapFragment);
        }
    };

    private Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> buildFragmentAndroidInjectorMap() {
        Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap = new HashMap<>();
        fragmentInjectorMap.put(ContactMapFragment.class, new ContactMapFragmentAndroidInjector(() -> mockContactMapPresenter));
        return fragmentInjectorMap;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);

    private String testLookup = "testLookup";

    private ContactMapFragment contactMapFragment = ContactMapFragment.newInstance(testLookup);

    @Mock
    private ContactMapPresenter mockContactMapPresenter;

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
        assertTrue(isMock(contactMapFragment.presenter));
        assertSame(contactMapFragment.presenter, mockContactMapPresenter);
        assertTrue(activityRule.getActivity().getSupportFragmentManager().getFragments().contains(contactMapFragment));
    }

    @UiThreadTest
    @Test
    public void testInitialPointLoading() {
        verify(mockContactMapPresenter).loadPinPointForContact(testLookup);
    }

    @Test
    public void testTapOnMapPresenterCall() throws UiObjectNotFoundException {
        device.findObject(new UiSelector()
                .descriptionContains("Google Map")
        ).click();
        verify(mockContactMapPresenter).submitGeodecoding(any(PinPoint.class));
    }

    @Test
    public void testApplyingAddress() throws Throwable {
        AddressBean address = new AddressBean(1, 1, "address");
        activityRule.runOnUiThread(() -> {
            contactMapFragment.applyContactPinPoint(address);
        });

        UiObject addressMarker = device.findObject(new UiSelector()
                .descriptionContains(address.getAddressName())
        );
        assertNotNull(addressMarker);
    }

    @Test
    public void testSubmittingAfterAddressFound() throws Throwable {
        AddressBean address = new AddressBean(1, 1, "address");
        activityRule.runOnUiThread(() -> {
            contactMapFragment.onGeodecodingFinished(address);
        });
        onView(withId(R.id.contact_save_button)).perform(click());
        verify(mockContactMapPresenter).submitPinPoint(testLookup, address);
    }

    @Test
    public void testSubmitButtonHidingOnGoedecodingFailed() throws Throwable {
        AddressBean address = new AddressBean(1, 1, "");
        activityRule.runOnUiThread(() -> {
            contactMapFragment.onGeodecodingFinished(address);
        });
        onView(withId(R.id.contact_save_button)).check(matches(not(isDisplayed())));
    }
}