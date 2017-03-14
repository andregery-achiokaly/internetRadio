package com.somenameofpackage.internetradiowithmosby.ui;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.model.ApplicationComponentTest;
import com.somenameofpackage.internetradiowithmosby.model.RepositoryModuleTest;

import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class RadioApplicationTest extends Application{

    public Context getApplication(){
       return getInstrumentation().getTargetContext().getApplicationContext();
    }

    static ApplicationComponentTest component;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerRadioApplicationTest.builder()
                .repositoryModuleTest(
                        new RepositoryModuleTest(new RadioApplicationTest().getApplication(),
                                true,
                                true,
                                true,
                                true)).build();
    }

    public static ApplicationComponentTest getComponent() {
        return component;
    }
}