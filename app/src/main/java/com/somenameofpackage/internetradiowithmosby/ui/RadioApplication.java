package com.somenameofpackage.internetradiowithmosby.ui;

import android.app.Application;
import android.content.Intent;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.DaggerAppComponent;
import com.somenameofpackage.internetradiowithmosby.model.RepositoryModule;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;

public class RadioApplication extends Application {
    static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        serviceStart();
    }

    private void initDagger() {
        component = DaggerAppComponent.builder().repositoryModule(new RepositoryModule(RadioApplication.this)).build();
    }

    private void serviceStart() {
        getApplicationContext().startService(new Intent(getApplicationContext(), RadioService.class));
    }

    public static AppComponent getComponent() {
        return component;
    }
}
