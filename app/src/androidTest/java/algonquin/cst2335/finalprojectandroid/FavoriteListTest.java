/*
 * FileName: FavoriteListTest.java
 * Purpose: Tests the Favorite List functionality in the MainActivity.
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
 * Tests the Favorite List functionality in the MainActivity.
 * Ensures that the Favorite List is accessible and displays correctly when triggered from the menu.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavoriteListTest {

    /**
     * ActivityScenarioRule initializes the activity before each test run.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that the "FAVORITE LIST" text is displayed as a title on the Favorite List page,
     * confirming that the Favorite List is being shown.
     */
    @Test
    public void favoriteListTest() {
        // Direct use of Thread.sleep is discouraged; it's better to employ Idling Resources
        try {
            Thread.sleep(5576);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulates a click on the "Deezer song search" button
        onView(withId(R.id.btnDeezerSongSearch)).perform(click());

        // Simulates a click on the "My Favorite" menu item to display the Favorite List
        onView(withId(R.id.my_favorite)).perform(click());

        // Verifies that the "FAVORITE LIST" title is displayed on the screen
        onView(withId(R.id.favoriteTitle)).check(matches(withText(R.string.favorite_list_text)));
    }


}