package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class RealmModule {
    private Application application;

    private void initRealm() {
        Realm.init(application);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public RealmModule(Application application) {
        this.application = application;
        initRealm();
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
