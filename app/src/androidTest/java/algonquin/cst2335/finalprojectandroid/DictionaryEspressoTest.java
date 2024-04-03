package algonquin.cst2335.finalprojectandroid;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import algonquin.cst2335.finalprojectandroid.dictionary.Dictionary;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DictionaryEspressoTest {

    @Rule
    public ActivityScenarioRule<Dictionary> activityScenarioRule = new ActivityScenarioRule<>(Dictionary.class);

    @Test
    public void testDictionarySearch() {
        // Replace these with appropriate IDs for your search view, button, and result view
        String searchText = "test";
        int searchViewId = R.id.searchEditText;
        // Type text into the search view
        onView(withId(searchViewId)).perform(replaceText(searchText), closeSoftKeyboard());
    }

    @Test
    public void testDictionarySearch2() {
        // Replace these with appropriate IDs for your search view, button, and result view
        String searchText = "test";
        int searchButtonId = R.id.searchButton;
        // Click the search button
        onView(withId(searchButtonId)).perform(click());
    }

    @Test
    public void testDictionarySearch3() {
        // Replace these with appropriate IDs for your search view, button, and result view
        String searchText = "test";
        int resultViewId = R.id.recyclerView;
        // Check if the result view is displayed
        onView(withId(resultViewId)).check(matches(isDisplayed()));

    }

    @Test
    public void testDictionaryToolbarVisibility() {
        int toolbarId = R.id.toolbar;

        // Check if the toolbar is displayed
        onView(withId(toolbarId)).check(matches(isDisplayed()));
    }

    @Test
    public void testDefinitionTextViewVisibility() {
        int definitionTextViewId = R.id.dictionaryTextView;

        // Check if the TextView displaying word definitions is displayed
        onView(withId(definitionTextViewId)).check(matches(isDisplayed()));
    }
}
