package com.somenameofpackage.internetradiowithmosby.model.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;

import java.io.ByteArrayOutputStream;

import io.realm.RealmResults;

public class RadioStations {
    private final DataBase dataBase;

    public RadioStations(Context context) {
        dataBase = new StationsRelamDB(context);
    }

    public void firstInitial(Context context) {
        addStation(context.getString(R.string.best_fm_name),
                context.getString(R.string.best_fm_source),
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));

        addStation(context.getString(R.string.jam_fm_name),
                context.getString(R.string.jam_fm_source),
                BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher));
    }

    public RealmResults<RadioStation> getStations() {
        return dataBase.getStations();
    }

    public void addStation(String name, String source, Bitmap icon) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);

        RadioStation radioStation = new RadioStation(name, source, stream.toByteArray());
        dataBase.addStation(radioStation);
    }

    public void addStation(String name, String source, byte[] icon) {
        RadioStation radioStation = new RadioStation(name, source, icon);
        dataBase.addStation(radioStation);
    }

    public void removeStation(RadioStation radioStation) {
        dataBase.deleteStation(radioStation.getSource());
    }

    public void removeStation(String source) {
        dataBase.deleteStation(source);
    }

    public String getPlayingStationSource() {
        return dataBase.getPlayingStationSource();
    }

    public void setPlayStation(String source) {
        if (source != null)
            dataBase.setPlayStation(source);
    }

    public void closeBD() {
        dataBase.closeBD();
    }
}
