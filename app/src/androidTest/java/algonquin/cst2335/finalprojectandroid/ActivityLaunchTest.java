/*
 * FileName: ActivityLaunchTest.java
 * Purpose: This test class is used to ensure that the MainActivity launches successfully.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test class is used to ensure that the MainActivity launches successfully.
 * The {@link ActivityScenarioRule} provides functional testing of a single activity.
 * The tests included in this class verify that the activity under test is launched and
 * is not null, indicating that the activity was started properly without any issues.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActivityLaunchTest {

    /**
     * ActivityScenarioRule is a JUnit {@link Rule} to define an activity scenario.
     * By using this rule, the testing framework will take care of launching the
     * activity before the test starts and shutting it down after it finishes.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that the MainActivity is launched and is not null.
     * This test passes if the MainActivity is initiated properly.
     */
    @Test
    public void testActivityLaunch() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat("MainActivity should not be null", activity, not(nullValue()));
        });
    }
}