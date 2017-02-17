package com.somenameofpackage.internetradiowithmosby.ui;


import android.app.Application;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RadioApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.create();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
//        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static AppComponent getComponent() {
        return component;
    }
}
