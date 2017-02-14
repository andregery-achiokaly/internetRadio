package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;

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
