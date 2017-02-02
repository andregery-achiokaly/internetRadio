package com.somenameofpackage.internetradiowithmosby.model.db.SQLiteHelpDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Bitmap;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStation;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class StationsDBHelper extends SQLiteOpenHelper implements DataBase {
    public StationsDBHelper(Context context) {
        super(context, null, new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                return null;
            }
        }, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + RadioStation.getNameTable()
                + " ("
                + RadioStation.getNameFieldName() + " text,"
                + RadioStation.getSourceFieldName() + " text primary key,"
                + RadioStation.getIsPlayFieldName() + " boolean,"
                + RadioStation.getImageFieldName() + " blob"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void addStation(RadioStation radioStation) {
        addStation(radioStation.getName(), radioStation.getSource(), radioStation.getImage());
    }

    private void addStation(String name, String source, byte[] bitmap) {
        ContentValues cv = new ContentValues();

        cv.put(RadioStation.getIsPlayFieldName(), 0);
        cv.put(RadioStation.getNameFieldName(), name);
        cv.put(RadioStation.getSourceFieldName(), source);
        cv.put(RadioStation.getImageFieldName(), bitmap);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.endTransaction();
        db.close();
    }

    @Override
    public String getPlayingStationSource() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = RadioStation.getIsPlayFieldName() + " = ?";
        String[] selectionArgs = new String[]{"1"};
        Cursor c = db.query(RadioStation.getNameTable(),
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (c == null) return null;
        int sourceCI = c.getColumnIndex(RadioStation.getSourceFieldName());
        String source = null;
        if (c.moveToFirst()) source = c.getString(sourceCI);
        c.close();
        db.close();
        return source;
    }

    @Override
    public List<RadioStation> getStations() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(RadioStation.getNameTable(), null, null, null, null, null, null);
        List<RadioStation> result = new LinkedList<>();
        if (c == null) return result;
        int nameCI = c.getColumnIndex(RadioStation.getNameFieldName());
        int sourceCI = c.getColumnIndex(RadioStation.getSourceFieldName());
        int isPlayCI = c.getColumnIndex(RadioStation.getIsPlayFieldName());
        int imageCI = c.getColumnIndex(RadioStation.getImageFieldName());
        if (c.moveToFirst()) {
            do {
                RadioStation radioStation = new RadioStation();
                radioStation.setName(c.getString(nameCI));
                radioStation.setSource(c.getString(sourceCI));
                radioStation.setImage(c.getBlob(imageCI));
                if (c.getInt(isPlayCI) == 1) radioStation.setPlay(true);
                else radioStation.setPlay(false);
                result.add(radioStation);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    @Override
    public void setPlayStation(String source) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(RadioStation.getIsPlayFieldName(), 1);
        db.beginTransaction();
        db.update(RadioStation.getNameTable(),
                cv,
                RadioStation.getSourceFieldName() + " = ?",
                new String[]{source});
        db.endTransaction();
    }

    @Override
    public void closeBD() {
        this.close();
    }

    @Override
    public void clearBD() {

    }

    @Override
    public void deleteStation(String source) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete(RadioStation.getNameTable(),
                RadioStation.getSourceFieldName() + " = " + source,
                null);
        db.endTransaction();
    }
}
