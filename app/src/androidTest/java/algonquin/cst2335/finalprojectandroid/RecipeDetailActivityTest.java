package algonquin.cst2335.finalprojectandroid;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {

    @Rule
    public ActivityScenarioRule<RecipeDetailActivity> activityRule =
            new ActivityScenarioRule<>(RecipeDetailActivity.class);


    @Test
    public void testRecipeDetailsDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", 1); // Example ID
        intent.putExtra("RECIPE_TITLE", "Test Recipe");
        intent.putExtra("RECIPE_IMAGE_URL", "http://example.com/test.jpg");

        // Launch the activity with the intent
        try (ActivityScenario<RecipeDetailActivity> scenario = ActivityScenario.launch(intent)) {
            // Perform your checks here
            onView(withId(R.id.detailTextViewRecipeTitle)).check(matches(withText("Test Recipe")));
        }
    }


}
