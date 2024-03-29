package algonquin.cst2335.finalprojectandroid;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeezerSongActivityTest {

    @Rule
    public ActivityScenarioRule<DeezerSongActivity> activityRule =
            new ActivityScenarioRule<>(DeezerSongActivity.class);

    @Test
    public void deezerSongSearchTest() {
        // Type the artist's name into the EditText field
        onView(withId(R.id.artistText)).perform(typeText("exo"), closeSoftKeyboard());

        // Click the "Search" button
        onView(withId(R.id.searchButton)).perform(click());

        // Use an Idling Resource to wait for the RecyclerView to be populated
        // This step is crucial to ensure that the network request has completed before checking the items
        // Alternatively, you can use Thread.sleep(), but it's not recommended
        // For example: Thread.sleep(2000);

        // Scroll to the item with the "Love Shot" text to ensure it is visible on screen
        onView(withId(R.id.artistItem))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Love Shot"))));

        // Check that the "Love Shot" text is displayed in the RecyclerView
        onView(allOf(withId(android.R.id.text1), withText("Love Shot")))
                .check(matches(isDisplayed()));
    }
}