package com.somenameofpackage.internetradiowithmosby.model.SQLiteHelpDB;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import com.somenameofpackage.internetradiowithmosby.model.realmDB.RadioStation;

public class StationsDBHelper extends SQLiteOpenHelper {
    public StationsDBHelper(Context context) {
        super(context, "RadioStations", new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                return null;
            }
        }, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RadioStation.getNameTable()
                + " ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "source text,"
                + "isPlay boolean,"
                + "image blob"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
