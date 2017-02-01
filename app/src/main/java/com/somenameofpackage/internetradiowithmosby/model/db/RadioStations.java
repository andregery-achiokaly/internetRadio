package com.somenameofpackage.internetradiowithmosby.model.db;

import android.content.Context;
import android.graphics.Bitmap;

import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.presenter.DBChangeListener;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class RadioStations{
    private List<RadioStation> radioStations;
    private DataBase dataBase;
    List<DBChangeListener> listeners = new LinkedList<>();

    public RadioStations(Context context) {
        radioStations = new LinkedList<>();
        dataBase = new StationsRelamDB(context);
        radioStations.addAll(dataBase.getStations());
    }

    public RadioStations(Context context, List<RadioStation> r) {
        this(context);
        radioStations.addAll(r);
    }

    public RadioStations(Context context, List<RadioStation> r, DBChangeListener listener) {
        this(context);
        radioStations.addAll(r);
        listeners.add(listener);
    }

    public List<RadioStation> getStations() {
        return radioStations;
    }

    public void addStation(String name, String source, Bitmap icon) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);

        RadioStation radioStation = new RadioStation(name, source, stream.toByteArray());
        dataBase.addStation(radioStation);
        if (!radioStations.contains(radioStation)) radioStations.add(radioStation);
    }

    public void addStation(String name, String source, byte[] icon) {
        RadioStation radioStation = new RadioStation(name, source, icon);
        dataBase.addStation(radioStation);
        if (!radioStations.contains(radioStation)) radioStations.add(radioStation);
    }

    public void removeStation(RadioStation radioStation) {
        dataBase.deleteStation(radioStation.getSource());
    }

    public void removeStation(String source) {
        dataBase.deleteStation(source);
        for (int i = 0; i < radioStations.size(); i++) {
            if (radioStations.get(i).getSource().equals(source)) {
                radioStations.remove(i);
                break;
            }
        }
    }

    public RadioStation get(int index) {
        return radioStations.get(index);
    }

    public int size() {
        return radioStations.size();
    }

    public String getPlayingStationSource() {
        return dataBase.getPlayingStationSource();
    }

    public void setPlayStation(String source) {
        dataBase.setPlayStation(source);
    }

    public void closeBD(){
        dataBase.closeBD();
    }

    private void updateListeners(){
        
    }
}
