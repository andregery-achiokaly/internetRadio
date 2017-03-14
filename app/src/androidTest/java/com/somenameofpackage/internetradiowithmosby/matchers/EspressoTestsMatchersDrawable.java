package com.somenameofpackage.internetradiowithmosby.matchers;

import android.view.View;

import org.hamcrest.Matcher;

public class EspressoTestsMatchersDrawable {
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }
}
