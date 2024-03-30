/*
 * FileName: AlertDialogTest.java
 * Purpose: Tests the display of an AlertDialog in the MainActivity.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
 * Tests the display of an AlertDialog in the MainActivity.
 * The test ensures that when the AlertDialog is triggered,
 * the expected message is displayed to the user.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AlertDialogTest {

    /**
     * ActivityScenarioRule launches a given activity before the test starts and
     * closes it after the test.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Checks that the AlertDialog displays the correct informational message to the user.
     * It simulates the user actions to open the dialog and then verifies the text content.
     */
    @Test
    public void alertDialogTest() {
        // Directly accessing sleep is not recommended; instead, use Idling Resources
        try {
            Thread.sleep(5838);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate user action to click the button that shows the AlertDialog
        onView(withId(R.id.btnDeezerSongSearch)).perform(click());

        // Simulate user action to open the information dialog
        onView(withId(R.id.info)).perform(click());

        // Check that the AlertDialog is displayed with the correct message
        onView(withId(android.R.id.message))
                .check(matches(withText("Here's how to use this app:\n\n1. Enter the name of the artist in the search field.\n2. Click the Search button to find songs by the artist.\n3. Tap on a song to see more details about it.\n4. Press the Add to Favorites button to save the song to your favorites list.\n5. Access the menu to navigate through the app.\n6. In the Favorites list, click on a song to view its details.\n7. To remove a song from the list, press the Delete button.")))
                .check(matches(isDisplayed()));
    }


}