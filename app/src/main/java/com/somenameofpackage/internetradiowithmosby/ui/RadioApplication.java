package com.somenameofpackage.internetradiowithmosby.ui;


import android.app.Application;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.DaggerAppComponent;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.RealmModule;

public class RadioApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().realmModule(new RealmModule(RadioApplication.this)).build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
