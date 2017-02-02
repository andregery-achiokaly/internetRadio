package com.somenameofpackage.internetradiowithmosby.model.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.SQLiteHelpDB.StationsDBHelper;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.DBChangeListener;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class RadioStations {
    private DataBase dataBase;
    private List<DBChangeListener> listeners = new LinkedList<>();

    public RadioStations(Context context) {
        dataBase = new StationsDBHelper(context);
        updateListeners();
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

    public List<RadioStation> getStations() {
        return dataBase.getStations();
    }

    public void addStation(String name, String source, Bitmap icon) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);

        RadioStation radioStation = new RadioStation(name, source, stream.toByteArray());
        dataBase.addStation(radioStation);

        updateListeners();
    }

    public void addStation(String name, String source, byte[] icon) {
        RadioStation radioStation = new RadioStation(name, source, icon);
        dataBase.addStation(radioStation);
        updateListeners();
    }

    public void removeStation(RadioStation radioStation) {
        dataBase.deleteStation(radioStation.getSource());
        updateListeners();
    }

    public void removeStation(String source) {
        dataBase.deleteStation(source);
        updateListeners();
    }

    public String getPlayingStationSource() {
        return dataBase.getPlayingStationSource();
    }

    public void setPlayStation(String source) {
        dataBase.setPlayStation(source);
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    private void updateListeners() {
        for (DBChangeListener listener : listeners) listener.update();
    }

    public void addListener(DBChangeListener dbChangeListener) {
        listeners.add(dbChangeListener);
    }
}
