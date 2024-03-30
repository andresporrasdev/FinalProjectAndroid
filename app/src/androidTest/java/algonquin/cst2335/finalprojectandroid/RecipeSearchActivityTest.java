package algonquin.cst2335.finalprojectandroid;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * File name: RecipeSearchActivityTest.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 * Test class for {@link RecipeSearchActivity} to verify the correct behavior of the activity.
 * Uses Espresso for UI testing to simulate user interactions and verify the state of the activity.
 *
 */
public class RecipeSearchActivityTest {

    /**
     * Rule to launch the {@link RecipeSearchActivity} before each test method.
     * This rule provides functional testing of a single activity.
     */
    @Rule
    public ActivityScenarioRule<RecipeSearchActivity> activityRule =
            new ActivityScenarioRule<>(RecipeSearchActivity.class);

    /**
     * Tests that the {@link RecipeSearchActivity} is not null when launched,
     * ensuring the activity can be instantiated and displayed.
     */
    @Test
    public void testActivityLaunch() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(activity, notNullValue());
        });
    }

    /**
     * Tests that a Snackbar appears when attempting a search with an empty query.
     * This verifies that the application correctly handles empty search queries by notifying the user.
     */
    @Test
    public void testSnackbarAppearanceOnEmptySearch() {
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.et_err)));
    }

    /**
     * Tests that the last search term entered by the user is saved and restored correctly
     * using SharedPreferences. This simulates the scenario where a user exits and re-enters
     * the application, expecting their last search term to be remembered.
     */
    @Test
    public void testSharedPreferencesSavesLastSearch() {
        String testSearchTerm = "chicken";

        onView(withId(R.id.recipeSearchEditText)).perform(typeText(testSearchTerm), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        activityRule.getScenario().recreate();

        onView(withId(R.id.recipeSearchEditText)).check(matches(withText(testSearchTerm)));
    }

    /**
     * Tests that a valid search query can be entered into the search field and submitted.
     * This test simulates a user entering a search term and pressing the search button,
     * but does not verify the result of the search.
     */
    @Test
    public void testValidSearchInput() {
        onView(withId(R.id.recipeSearchEditText)).perform(typeText("pasta"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    /**
     * Tests that the user guide AlertDialog is displayed when the appropriate menu item is selected.
     * This verifies that the user can access help information from within the application.
     */
    @Test
    public void testUserGuideAlertDialog() {
        Espresso.openContextualActionModeOverflowMenu();
        Espresso.onView(withText(R.string.info)).perform(ViewActions.click());

        Espresso.onView(CoreMatchers.allOf(withId(android.R.id.message), withText(R.string.guide_message), isDisplayed()))
                .check(ViewAssertions.matches(withText(R.string.guide_message)));
    }

}