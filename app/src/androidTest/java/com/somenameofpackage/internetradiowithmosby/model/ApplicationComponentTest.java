package com.somenameofpackage.internetradiowithmosby.model;

import com.somenameofpackage.internetradiowithmosby.AppComponent;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDBTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RepositoryModuleTest.class)
public interface ApplicationComponentTest extends AppComponent {
    public void injectRelamDBTest(StationsRelamDBTest stationsRelamDBTest);
}
