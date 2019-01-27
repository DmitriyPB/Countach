package com.testing.android.countach.presentation.details;

import android.Manifest;
import android.widget.ProgressBar;

import com.testing.android.countach.CountachApp;
import com.testing.android.countach.MainActivity;
import com.testing.android.countach.R;
import com.testing.android.countach.TestUtils;
import com.testing.android.countach.data.ContactBean;
import com.testing.android.countach.presentation.contactmap.ContactMapFragment;
import com.testing.android.countach.presentation.contactmap.ContactMapFragmentAndroidInjector;
import com.testing.android.countach.presentation.contactmap.ContactMapPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import dagger.android.AndroidInjector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.internal.util.MockUtil.isMock;

@RunWith(AndroidJUnit4.class)
public class ContactDetailsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            CountachApp app = ((CountachApp) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
            app.activityDispatchingAndroidInjector = TestUtils.createFakeMainActivityInjector(buildFragmentAndroidInjectorMap(), () -> contactDetailsFragment);
        }
    };

    private Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> buildFragmentAndroidInjectorMap() {
        Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap = new HashMap<>();
        fragmentInjectorMap.put(ContactMapFragment.class, new ContactMapFragmentAndroidInjector(() -> mockContactMapPresenter));
        fragmentInjectorMap.put(ContactDetailsFragment.class, new ContactDetailsFragmentAndroidInjector(() -> mockContactDetailsPresenter));
        return fragmentInjectorMap;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);

    private String testLookup = "testLookup";
    private ContactDetailsFragment contactDetailsFragment = ContactDetailsFragment.newInstance(testLookup);

    @Mock
    private ContactDetailsPresenter mockContactDetailsPresenter;
    @Mock
    private ContactMapPresenter mockContactMapPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityRule.launchActivity(null);
    }

    @UiThreadTest
    @Test
    public void testPresenterIsMocked() {
        Assert.assertTrue(isMock(contactDetailsFragment.presenter));
        Assert.assertSame(contactDetailsFragment.presenter, mockContactDetailsPresenter);
        Assert.assertTrue(activityRule.getActivity().getSupportFragmentManager().getFragments().contains(contactDetailsFragment));
    }

    @Test
    public void testClickOnEditLocationButtonOpensMap() {
        onView(withId(R.id.button_edit_location))
                .perform(click());
        onView(withId(R.id.contact_map_view)).check(matches(isDisplayed()));
    }

    @UiThreadTest
    @Test
    public void testInitialDetailsLoading() {
        Mockito.verify(mockContactDetailsPresenter).loadContactDetails(testLookup);
    }

    @Test
    public void testApplyingContact() throws Throwable {
        String email = "44@jo.com";
        String phoneNumber = "22-44-55";
        String name = "alex";
        ContactBean contact = new ContactBean(name, phoneNumber, email, testLookup);

        activityRule.runOnUiThread(() -> {
            contactDetailsFragment.applyContact(contact);
        });

        onView(withId(R.id.text_view_name)).check(matches(withText(name)));
        onView(withId(R.id.text_view_email)).check(matches(withText(email)));
        onView(withId(R.id.text_view_phone)).check(matches(withText(phoneNumber)));
    }

    @Test
    public void testShowProgressBar() throws Throwable {
        onView(isAssignableFrom(ProgressBar.class)).perform(TestUtils.replaceProgressBarDrawable());
        activityRule.runOnUiThread(() -> {
            contactDetailsFragment.showLoadingIndicator(true);
        });
        onView(withId(R.id.progress_bar_contact_details)).check(matches(isDisplayed()));
    }

    @Test
    public void testHideProgressBar() throws Throwable {
        activityRule.runOnUiThread(() -> {
            contactDetailsFragment.showLoadingIndicator(false);
        });

        onView(withId(R.id.progress_bar_contact_details)).check(matches(not(isDisplayed())));
    }
}