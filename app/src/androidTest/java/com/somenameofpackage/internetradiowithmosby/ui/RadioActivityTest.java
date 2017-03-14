package com.somenameofpackage.internetradiowithmosby.ui;

import android.support.test.rule.ActivityTestRule;

import com.somenameofpackage.internetradiowithmosby.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RadioActivityTest {
    @Rule
    public ActivityTestRule<RadioActivity> activityTestRule = new ActivityTestRule<>(RadioActivity.class);

    @Test
    public void onOptionsItemSelectedTest() {
        onView(withId(R.id.add_station_menu_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void onCreateOptionsMenuTest() {
        onView(withId(R.id.add_station_menu_btn)).perform(click());
        onView(withId(R.id.dialog_add_station)).check(matches(isDisplayed()));
    }
}