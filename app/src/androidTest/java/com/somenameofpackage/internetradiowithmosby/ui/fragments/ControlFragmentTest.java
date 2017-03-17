package com.somenameofpackage.internetradiowithmosby.ui.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.ui.RadioActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ControlFragmentTest {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(RadioActivity.class);

    @Test
    public void testView() {
        ControlFragment myFragment = startMyFragment();
        getInstrumentation().waitForIdleSync();
        myFragment.showStatus(RadioStatus.Error);

        onView(withId(R.id.fragment_radio_layout)).check(matches(isDisplayed()));
//        onView(withId(R.id.play_btn)).check(matches((R.mipmap.ic_error_black_24dp)));
        // MyFragment has a recyclerview.
        //OnEvent is EventBus callback that in this test contains no data.
        //I want the fragment to display empty list text and hide the recyclerView
    }

    private ControlFragment startMyFragment() {
        FragmentActivity activity = (FragmentActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        ControlFragment myFragment = ControlFragment.newInstance();
        transaction.add(myFragment, "ControlFragment");
        transaction.commit();
        return myFragment;
    }
}

