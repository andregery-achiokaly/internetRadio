package com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import java.util.LinkedList;
import java.util.List;

public class SqliteDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 8;
    private static final String DATABASE_NAME = "DATABASE_STATIONS";
    private final ContentResolver resolver;

    private static final String DATABASE_CREATE_TABLE_STATIONS =
            "create table " + Station.TABLE_NAME + " ("
                    + StationContracts.Stations.COLUMN_NAME_ID + " integer primary key autoincrement, "
                    + StationContracts.Stations.COLUMN_NAME_NAME + " string , "
                    + StationContracts.Stations.COLUMN_NAME_SOURCE + " string , "
                    + StationContracts.Stations.COLUMN_NAME_IS_PLAY + " integer);";

    public SqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        resolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_STATIONS);
        db.execSQL("insert into " + StationContracts.Stations.TABLE_NAME + " values (1, 'RadioROKS', 'http://online-radioroks.tavrmedia.ua/RadioROKS', 1);");
        for (int i = 2; i < 1000; i++) {
            db.execSQL("insert into " + StationContracts.Stations.TABLE_NAME + " values (" + i + ", 'europaplus', 'http://cast.radiogroup.com.ua:8000/europaplus', 0);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StationContracts.Stations.TABLE_NAME);
        onCreate(db);
    }

    private void getStationByID(int id) {
        String[] stations = null;
        Cursor c = resolver.query(
                StationContracts.Stations.CONTENT_URI,
                StationContracts.Stations.DEFAULT_PROJECTION,
                StationContracts.Stations.COLUMN_NAME_ID + "=?",
                new String[]{"" + id},
                null);

        if (c != null) {
            if (c.moveToFirst()) {
                stations = new String[c.getCount()];
                int i = 0;

                do {
                    String firstName = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_NAME));
                    String secondName = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_SOURCE));
                    int score = c.getInt(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_IS_PLAY));
                    stations[i] = firstName + " " + secondName + " | " + score;
                    i++;
                } while (c.moveToNext());
            }
            c.close();
        }
    }


    public List<Station> getStations() {
        List<Station> stations = new LinkedList<>();
        Cursor c = resolver.query(
                StationContracts.Stations.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_NAME));
                    String source = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_SOURCE));
                    int isPlay = c.getInt(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_IS_PLAY));
                    int id = c.getInt(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_ID));
                    stations.add(new Station(name, source, isPlay, id));
                } while (c.moveToNext());
            }
            c.close();
        }
        return stations;
    }

    public void addStation(Station station) {

    }

    public void closeBD() {

    }

    public void deleteStation(String source) {

    }

    public boolean updatedetails(Station station) {
        ContentValues args1 = new ContentValues();
        args1.put(StationContracts.Stations.COLUMN_NAME_IS_PLAY, 0);
        resolver.update(StationContracts.Stations.CONTENT_URI,
                args1,
                null,
                null);

        ContentValues args = new ContentValues();
        args.put(StationContracts.Stations.COLUMN_NAME_ID, station.getId_key());
        args.put(StationContracts.Stations.COLUMN_NAME_NAME, station.getName());
        args.put(StationContracts.Stations.COLUMN_NAME_SOURCE, station.getSource());
        args.put(StationContracts.Stations.COLUMN_NAME_IS_PLAY, 1);
        int i = resolver.update(StationContracts.Stations.CONTENT_URI,
                args,
                StationContracts.Stations.COLUMN_NAME_ID + " =?",
                new String[]{"" + station.getId_key()});
        return i > 0;
    }

    public void setPlayingStationSource(Station playingStationSource) {
        updatedetails(playingStationSource);
    }



    public Station getCurrentStation() {
        Cursor c = resolver.query(
                StationContracts.Stations.CONTENT_URI,
                StationContracts.Stations.DEFAULT_PROJECTION,
                StationContracts.Stations.COLUMN_NAME_IS_PLAY + "=?",
                new String[]{"1"},
                null);

        if (c != null) {
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_NAME));
                String source = c.getString(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_SOURCE));
                int isPlay = c.getInt(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_IS_PLAY));
                int id = c.getInt(c.getColumnIndex(StationContracts.Stations.COLUMN_NAME_ID));
                return new Station(name, source, isPlay, id);
            }
            c.close();
        }
        return null;
    }
}
