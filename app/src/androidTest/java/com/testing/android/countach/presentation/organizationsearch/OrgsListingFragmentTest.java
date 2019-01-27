package com.testing.android.countach.presentation.organizationsearch;

import android.Manifest;

import com.testing.android.countach.CountachApp;
import com.testing.android.countach.MainActivity;
import com.testing.android.countach.R;
import com.testing.android.countach.TestUtils;
import com.testing.android.countach.data.ContactBean;
import com.testing.android.countach.data.organizationsearch.OrgSearchResultBean;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import dagger.android.AndroidInjector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.util.MockUtil.isMock;

public class OrgsListingFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            CountachApp app = ((CountachApp) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
            app.activityDispatchingAndroidInjector = TestUtils.createFakeMainActivityInjector(buildFragmentAndroidInjectorMap(), () -> orgsListingFragment);
        }
    };

    private Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> buildFragmentAndroidInjectorMap() {
        Map<Class<? extends Fragment>, AndroidInjector<? extends Fragment>> fragmentInjectorMap = new HashMap<>();
        fragmentInjectorMap.put(OrgsListingFragment.class, new OrgsListingFragmentAndroidInjector(() -> mockOrgsListingPresenter));
        return fragmentInjectorMap;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS);

    private OrgsListingFragment orgsListingFragment = OrgsListingFragment.newInstance();

    @Mock
    private OrgsListingPresenter mockOrgsListingPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityRule.launchActivity(null);
    }

    @Test
    public void testPresenterIsMocked() {
        Assert.assertTrue(isMock(orgsListingFragment.presenter));
        Assert.assertSame(orgsListingFragment.presenter, mockOrgsListingPresenter);
        Assert.assertTrue(activityRule.getActivity().getSupportFragmentManager().getFragments().contains(orgsListingFragment));
    }

    @Test
    public void testSearchQuery() {
        String testQuery = "testQuery";

        onView(withId(R.id.search_button))
                .perform(click());
        onView(supportsInputMethods())
                .check(matches(isDisplayed()))
                .perform(typeText(testQuery));
        Mockito.verify(orgsListingFragment.presenter, Mockito.times(testQuery.length())).loadOrganizationByQuery(any());
        Mockito.verify(orgsListingFragment.presenter, Mockito.times(1)).loadOrganizationByQuery(testQuery);
    }

    @Test
    public void testApplyingResult() throws Throwable {
        LinkedList<OrgSearchResult> results = new LinkedList<>();
        results.add(new OrgSearchResultBean(
                new OrgBean(1L, "org 1", 1D, 1D),
                Arrays.asList(
                        new ContactBean("contact", "number", "mail@mail.com", "look1"),
                        new ContactBean("contact2", "number2", "mail2@mail.com", "look2")
                )
        ));
        results.add(new OrgSearchResultBean(
                new OrgBean(2L, "org 2", 2D, 2D),
                Arrays.asList(
                        new ContactBean("contact3", "number3", "mail3@mail.com", "look3"),
                        new ContactBean("contact4", "number4", "mail4@mail.com", "look4"),
                        new ContactBean("contact5", "number5", "mail5@mail.com", "look5")
                )
        ));
        activityRule.runOnUiThread(() -> {
            orgsListingFragment.applyResult(results);
        });
        onView(withId(R.id.recycler_view_org_list)).check(matches(hasChildCount(results.size())));
        onView(allOf(withId(R.id.recycler_view_orgs_contact_list), hasChildCount(2))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.recycler_view_orgs_contact_list), hasChildCount(3))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_view_orgs_contact_name), withText("contact"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_view_orgs_contact_name), withText("contact2"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_view_orgs_contact_name), withText("contact3"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_view_orgs_contact_name), withText("contact4"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.text_view_orgs_contact_name), withText("contact5"))).check(matches(isDisplayed()));
    }
}