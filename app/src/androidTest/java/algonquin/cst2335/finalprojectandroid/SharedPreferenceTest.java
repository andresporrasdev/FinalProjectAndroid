/*
 * FileName: SharedPreferenceTest.java
 * Purpose: Tests the persistence of user input across app restarts, ensuring that the Shared Preferences functionality is working as expected.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests the persistence of user input across app restarts, ensuring that the Shared Preferences functionality is working as expected.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SharedPreferenceTest {

    /**
     * ActivityScenarioRule initializes the activity before each test run.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Checks if the search input is preserved after navigating away from the main activity and then returning to it, mimicking user's return to the app.
     */
    @Test
    public void sharedPreferenceTest() {
        // Waits to match the app's execution delay
        try {
            Thread.sleep(5960);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Performs a click on the "Deezer song search" button
        onView(withId(R.id.btnDeezerSongSearch)).perform(click());

        // Enters text "exo" into the artist text field and closes the keyboard
        onView(withId(R.id.artistText))
                .perform(replaceText("exo"), closeSoftKeyboard());

        // Clicks the "Click" button to initiate the search
        onView(withId(R.id.searchButton)).perform(click());

        // Navigates to the "My Favorite" page
        onView(withId(R.id.my_favorite)).perform(click());

        // Returns to the main activity
        onView(withId(R.id.goHome)).perform(click());

        // Verifies that the text "exo" is still present in the artist text field
        onView(withId(R.id.artistText)).check(matches(withText("exo")));
    }


}