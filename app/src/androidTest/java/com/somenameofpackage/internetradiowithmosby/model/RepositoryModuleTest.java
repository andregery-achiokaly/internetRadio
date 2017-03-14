package com.somenameofpackage.internetradiowithmosby.model;

import android.content.Context;
import android.support.test.espresso.core.deps.dagger.Module;

import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.radio.Radio;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.RadioVisualizer;

import javax.inject.Singleton;

import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.mockito.Mockito.mock;

@Module
public class RepositoryModuleTest {
    private Context application;
    private boolean isMockedRealm;
    private boolean isMockedRadio;
    private boolean isMockedVisualizer;
    private boolean isMockedStationsRelamDB;

    public RepositoryModuleTest(Context context,
                                boolean mockRealm,
                                boolean mockRadio,
                                boolean mockVisualizer,
                                boolean mockStationsRelamDB) {
        this.application = application;
        this.isMockedRealm = mockRealm;
        this.isMockedRadio = mockRadio;
        this.isMockedVisualizer = mockVisualizer;
        this.isMockedStationsRelamDB = mockStationsRelamDB;

        if (!mockRealm) initRealm();
    }

    @Provides
    RadioVisualizer provideVisualizerModel() {
        return isMockedVisualizer ? mock(RadioVisualizer.class) : new RadioVisualizer();
    }

    @Provides
    Radio provideRadioModel() {
        return isMockedRadio ? mock(Radio.class) : new Radio();
    }

    @Provides
    StationsRelamDB provideStationsRelamDB() {
        return isMockedStationsRelamDB ? mock(StationsRelamDB.class) : new StationsRelamDB();
    }

    private void initRealm() {
        Realm.init(application);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return isMockedRealm ? mock(Realm.class) : Realm.getDefaultInstance();
    }
}