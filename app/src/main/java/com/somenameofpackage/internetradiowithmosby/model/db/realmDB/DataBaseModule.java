package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.app.Application;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Singleton
@Module
public class DataBaseModule {
    @Provides
    DataBase provideDataBase() {
        return new StationsRelamDB();
    }
}
