package com.somenameofpackage.internetradiowithmosby.ui;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class RadioApplicationTest {

    public Context getApplication(){
       return getInstrumentation().getTargetContext().getApplicationContext();
    }
}