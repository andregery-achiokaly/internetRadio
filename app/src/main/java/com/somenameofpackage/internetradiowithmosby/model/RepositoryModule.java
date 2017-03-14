package com.somenameofpackage.internetradiowithmosby.model;

import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.radio.Radio;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.RadioVisualizer;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
@Singleton
public class RepositoryModule {
    private RadioApplication application;

    public RepositoryModule(RadioApplication application) {
        this.application = application;
        initRealm();
    }

    @Provides
    RadioVisualizer provideVisualizerModel() {
        return new RadioVisualizer();
    }

    @Provides
    Radio provideRadioModel() {
        return new Radio();
    }

    @Provides
    StationsRelamDB provideStationsRelamDB() {
        return new StationsRelamDB();
    }

    private void initRealm() {
        Realm.init(application);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
