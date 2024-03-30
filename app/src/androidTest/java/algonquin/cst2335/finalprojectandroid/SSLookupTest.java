/*
 * Purpose: To perform automated UI tests on the Sunrise & Sunset Lookup App using Espresso.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class performs Espresso UI tests for the Sunrise & Sunset Lookup App. It tests various
 * functionalities like looking up sunrise and sunset times, saving locations, viewing saved locations,
 * and displaying the help dialog. It uses Espresso for interaction with UI components and checks
 * the correctness of the app's behavior.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SSLookupTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests the lookup functionality by entering a set of coordinates and checking if the sunrise and sunset
     * times are displayed correctly.
     */
    @Test
    public void lookupTest() {

        try {
            Thread.sleep(5163);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnSunriseSunsetlookup), withText("Sunrise SunSet lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.latitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("30"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.longitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("121"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.lookupButton), withText("Lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());



        ViewInteraction textView = onView(
                allOf(withId(R.id.sunriseTextView),
                        withParent(allOf(withId(R.id.main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText(containsString("M"))));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sunsetTextView),
                        withParent(allOf(withId(R.id.main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText(containsString("M"))));
    }

    /**
     * Tests the lookup functionality with negative latitude values to ensure the app correctly handles southern
     * hemisphere locations.
     */
    @Test
    public void lookupWithNegativeLatTest() {

        try {
            Thread.sleep(5895);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnSunriseSunsetlookup), withText("Sunrise SunSet lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.latitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("-33"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.longitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("151"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.lookupButton), withText("Lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.sunriseTextView),
                        withParent(allOf(withId(R.id.main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText(containsString("M"))));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sunsetTextView),
                        withParent(allOf(withId(R.id.main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText(containsString("M"))));

    }

    /**
     * Tests if a snackbar is displayed correctly after saving a location.
     */
    @Test
    public void snackbarTest() {

        try {
            Thread.sleep(5411);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnSunriseSunsetlookup), withText("Sunrise SunSet lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.viewFavoritesButton), withText("View Favorites"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.latitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("30"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.longitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("120"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.lookupButton), withText("Lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.saveLocationButton), withText("Save Location"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                9),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(com.google.android.material.R.id.snackbar_text), withText("Location saved successfully"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
    }

    /**
     * Tests saving a location's sunrise and sunset times and then verifies it can be viewed in the favorites list.
     */
    @Test
    public void saveLocationTest() {

        try {
            Thread.sleep(5676);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnSunriseSunsetlookup), withText("Sunrise SunSet lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.latitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.latitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("30"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.longitudeEditText),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("121"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.lookupButton), withText("Lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.saveLocationButton), withText("Save Location"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                9),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.favoritesRecyclerView),
                        childAtPosition(
                                withId(R.id.main),
                                12)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.linearLayout),
                        withParent(allOf(withId(R.id.favoritesRecyclerView),
                                withParent(withId(R.id.main)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.favoritesRecyclerView),
                        childAtPosition(
                                withId(R.id.main),
                                12)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();
    }

    /**
     * Tests the display of the help dialog to ensure the correct information is provided to the user.
     */
    @Test
    public void alertDialogTest() {
        try {
            Thread.sleep(5821);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnSunriseSunsetlookup), withText("Sunrise SunSet lookup"),
                        childAtPosition(
                                allOf(withId(R.id.main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        materialButton.perform(click());


        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        overflowMenuButton.perform(click());


        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialTextView = onView(
                allOf(withId(androidx.transition.R.id.title), withText("Help"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.message), withText("Welcome to the Sunrise & Sunset Lookup App! This application allows you to find the sunrise and sunset times for any location specified by its latitude and longitude.\n\nHow to Use:\n- Enter Latitude and Longitude: In the provided text fields, enter the latitude and longitude for which you wish to find sunrise and sunset times. Latitude and longitude should be in decimal format.\n- Lookup Sunrise and Sunset Times: Press the 'Lookup' button to retrieve the sunrise and sunset times for the entered location.\n- Save a Location: If you wish to save the location for future reference, press the 'Save Location' button after looking up the times. This will store the location in your favorites list.\n- View Saved Locations: Click on 'View Favorites' to see a list of all saved locations along with their sunrise and sunset times. You can click on any location in the list to view more details or delete it from the favorites.\n\nTips:\n- Make sure the latitude and longitude are correct to get accurate sunrise and sunset times.\n- Use negative values for latitudes south of the equator and longitudes west of the Prime Meridian.\n\nShould you need further assistance or encounter any issues, please feel free to contact our support team."),
                        withParent(withParent(withId(androidx.appcompat.R.id.scrollView))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(androidx.appcompat.R.id.alertTitle), withText("Help"),
                        withParent(allOf(withId(androidx.appcompat.R.id.title_template),
                                withParent(withId(androidx.appcompat.R.id.topPanel)))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));
    }

    /**
     * Utility method to help find child views within a parent view
      */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
