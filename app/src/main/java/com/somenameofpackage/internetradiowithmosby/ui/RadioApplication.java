package com.somenameofpackage.internetradiowithmosby.ui;


import android.app.Application;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.DaggerAppComponent;

public class RadioApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.create();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
