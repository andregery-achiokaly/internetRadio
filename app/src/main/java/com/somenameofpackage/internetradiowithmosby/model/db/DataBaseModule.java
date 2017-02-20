package com.somenameofpackage.internetradiowithmosby.model.db;

import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class DataBaseModule {
    @Provides
    DataBase provideDataBase() {
        return new StationsRelamDB();
    }
}
