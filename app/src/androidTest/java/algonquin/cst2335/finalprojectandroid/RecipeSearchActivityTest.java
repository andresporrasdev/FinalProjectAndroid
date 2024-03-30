package algonquin.cst2335.finalprojectandroid;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class RecipeSearchActivityTest {

    @Rule
    public ActivityScenarioRule<RecipeSearchActivity> activityRule =
            new ActivityScenarioRule<>(RecipeSearchActivity.class);

    @Test
    public void testSnackbarAppearanceOnEmptySearch() {
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.et_err)));
    }

    @Test
    public void testSharedPreferencesSavesLastSearch() {
        String testSearchTerm = "chicken";

        onView(withId(R.id.recipeSearchEditText)).perform(typeText(testSearchTerm), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        activityRule.getScenario().recreate();

        onView(withId(R.id.recipeSearchEditText)).check(matches(withText(testSearchTerm)));
    }



}