package com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class SqliteModule {
    private Application application;
    private SqliteDBHelper sqliteDBHelper;

    @Provides
    SqliteDBHelper provideSqliteDBHelper() {
        return sqliteDBHelper;
    }

    private void intiSqlite() {
        sqliteDBHelper = new SqliteDBHelper(application);
    }

    public SqliteModule(Application application) {
        this.application = application;
        intiSqlite();
    }
}
