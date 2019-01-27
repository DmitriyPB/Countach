package com.testing.android.countach.presentation.listing;

import android.Manifest;

import com.testing.android.countach.CountachApp;
import com.testing.android.countach.MainActivity;
import com.testing.android.countach.R;
import com.testing.android.countach.TestUtils;
import com.testing.android.countach.data.ContactBean;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.presentation.allpoints.AllPointsFragment;
import com.testing.android.countach.presentation.allpoints.AllPointsFragmentAndroidInjector;
import com.testing.android.countach.presentation.allpoints.AllPointsPresenter;
import com.testing.android.countach.presentation.details.ContactDetailsFragment;
import com.testing.android.countach.presentation.details.ContactDetailsFragmentAndroidInjector;
import com.testing.android.countach.presentation.details.ContactDetailsPresenter;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingFragment;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingFragmentAndroidInjector;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import dagger.android.AndroidInjector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.internal.util.MockUtil.isMock;


@RunWith(AndroidJUnit4.class)
public class ContactsListingFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            CountachApp app = ((CountachApp) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
            app.activityDispatchingAndroidInjector = TestUtils.createFakeMainActivityInjector(buildFragmentAndroidInjectorMap(), () -> contactsListingFragment);
        }
    };

    private Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> buildFragmentAndroidInjectorMap() {
        Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap = new HashMap<>();
        fragmentInjectorMap.put(ContactsListingFragment.class, new ContactsListingFragmentAndroidInjector(() -> mockContactsListingPresenter));
        fragmentInjectorMap.put(AllPointsFragment.class, new AllPointsFragmentAndroidInjector(() -> mockAllPointsPresenter));
        fragmentInjectorMap.put(OrgsListingFragment.class, new OrgsListingFragmentAndroidInjector(() -> mockOrgsListingPresenter));
        fragmentInjectorMap.put(ContactDetailsFragment.class, new ContactDetailsFragmentAndroidInjector(() -> mockContactDetailsPresenter));
        return fragmentInjectorMap;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);

    private ContactsListingFragment contactsListingFragment = ContactsListingFragment.newInstance();

    @Mock
    private ContactsListingPresenter mockContactsListingPresenter;
    @Mock
    private AllPointsPresenter mockAllPointsPresenter;
    @Mock
    private OrgsListingPresenter mockOrgsListingPresenter;
    @Mock
    private ContactDetailsPresenter mockContactDetailsPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityRule.launchActivity(null);
    }

    @Test
    public void testPresenterIsMocked() {
        Assert.assertTrue(isMock(contactsListingFragment.presenter));
        Assert.assertSame(contactsListingFragment.presenter, mockContactsListingPresenter);
        Assert.assertTrue(activityRule.getActivity().getSupportFragmentManager().getFragments().contains(contactsListingFragment));
    }

    @UiThreadTest
    @Test
    public void testInitialContactsLoading() {
        contactsListingFragment.loadContactsWithPermissionCheck();
        Mockito.verify(contactsListingFragment.presenter).loadContacts(anyString());
    }

    @Test
    public void testClickOnContact() throws Throwable {
        LinkedList<Contact> list = new LinkedList<>();
        list.add(new ContactBean("name", "7789", "email", "sndln"));
        list.add(new ContactBean("john", "6666", "email", "sndln"));

        activityRule.runOnUiThread(() -> {
            contactsListingFragment.applyContacts(list);
        });

        onView(withId(R.id.recycler_view_contact_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(
                withId(R.id.contact_phone_caption)
        ).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchQuery() {

        String testQuery = "testQuery";

        onView(withId(R.id.search_button))
                .perform(click());
        onView(supportsInputMethods())
                .check(matches(isDisplayed()))
                .perform(typeText(testQuery));
        Mockito.verify(contactsListingFragment.presenter, Mockito.times(testQuery.length())).loadContacts(any());
        Mockito.verify(contactsListingFragment.presenter, Mockito.times(1)).loadContacts(testQuery);
    }

    @Test
    public void testClickOnAllContactsOpensMap() {
        onView(withId(R.id.button_all_points))
                .perform(click());
        onView(withId(R.id.contact_map_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickOnOrgSearchOpensOrgSearch() {
        onView(withId(R.id.button_org_search))
                .perform(click());
        onView(withId(R.id.search_view_org_list)).check(matches(isDisplayed()));
    }
}