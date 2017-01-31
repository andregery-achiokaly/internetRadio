package com.somenameofpackage.internetradiowithmosby.model.SQLiteHelpDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.RadioStation;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class StationsDB implements DataBase {
    StationsDBHelper stationsDBHelper;

    public StationsDB(Context context) {
        stationsDBHelper = new StationsDBHelper(context);
    }

    @Override
    public void addStation(String name, String source, Bitmap bitmap) {
        ContentValues cv = new ContentValues();

        cv.put(RadioStation.getIdFieldName(), 1);
        cv.put(RadioStation.getIsPlayFieldName(), false);
        cv.put(RadioStation.getNameFieldName(), name);
        cv.put(RadioStation.getSourceFieldName(), source);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        cv.put(RadioStation.getImageFieldName(), stream.toByteArray());

        SQLiteDatabase db = stationsDBHelper.getWritableDatabase();
        db.insert(RadioStation.getNameTable(), null, cv);
        db.close();
    }

    @Override
    public String getPlayingSource() {
        return null;
    }

    @Override
    public int getNumberOfPlayingStation() {
        return 0;
    }

    @Override
    public List<RadioStation> getStations() {
        SQLiteDatabase db = stationsDBHelper.getReadableDatabase();
        Cursor c = db.query(RadioStation.getNameTable(), null, null, null, null, null, null);
        List<RadioStation> result = new LinkedList<>();

        int idCI = c.getColumnIndex(RadioStation.getIdFieldName());
        int nameCI = c.getColumnIndex(RadioStation.getNameFieldName());
        int sourceCI = c.getColumnIndex(RadioStation.getSourceFieldName());
        int isPlayCI = c.getColumnIndex(RadioStation.getIsPlayFieldName());
        int imageCI = c.getColumnIndex(RadioStation.getImageFieldName());

        do {
            RadioStation radioStation = new RadioStation();
            radioStation.setId(c.getInt(idCI));
            radioStation.setName(c.getString(nameCI));
            radioStation.setSource(c.getString(sourceCI));
            radioStation.setImage(c.getBlob(imageCI));
            if (c.getInt(isPlayCI) == 1) radioStation.setPlay(true);
            else radioStation.setPlay(false);

            result.add(radioStation);
        } while (c.moveToNext());
        return result;
    }

    @Override
    public void setPlayStation(String source) {

    }

    @Override
    public void closeBD() {

    }

    @Override
    public void clearBD() {

    }

    @Override
    public void deleteStation(String source) {

    }
}
