/*
 * FileName: InputTest.java
 * Purpose: Tests the input functionality for the search feature in the MainActivity.
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
 * Tests the input functionality for the search feature in the MainActivity.
 * It ensures that the input provided by the user is correctly reflected in the UI.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class InputTest {

    /**
     * ActivityScenarioRule initializes the activity before each test run.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that after entering text into the artist text field and clicking the search button,
     * the text remains as entered.
     */
    @Test
    public void inputTest() {
        // Waits to match the app's execution delay
        try {
            Thread.sleep(5864);
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

        // Checks that the artist text field still contains the text "exo"
        onView(withId(R.id.artistText)).check(matches(withText("exo")));
    }


}