package com.somenameofpackage.internetradiowithmosby.model.realmDB;

import android.content.Context;
import android.graphics.Bitmap;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StationsDB implements DataBase{
    private Realm realm;
    private static final String nameOfConfiguration = "Configuration1";

    public StationsDB(Context context) {

        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(nameOfConfiguration)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
    }

    public String getPlayingSource() {
        realm.beginTransaction();
        RadioStation radioStation = realm
                .where(RadioStation.class)
                .equalTo(RadioStation.getIsPlayFieldName(), true)
                .findFirst();

        if (radioStation == null) {
            radioStation = realm.where(RadioStation.class).findFirst();
        }
        realm.commitTransaction();
        return radioStation.getSource();
    }

    public int getNumberOfPlayingStation() {
        realm.beginTransaction();
        RealmResults<RadioStation> radioStations = realm.where(RadioStation.class).findAll();
        int id = 0;
        if (!radioStations.isEmpty()) {
            for (int i = 0; i < radioStations.size(); i++) {
                if (radioStations.get(i).isPlay()) break;
                id++;
            }
        }
        realm.commitTransaction();
        return id;
    }

    public void addStation(String stationName, String stationSource, Bitmap icon) {
        int id = getNextKey();
        realm.beginTransaction();
        RadioStation radioStation = new RadioStation();
        radioStation.setName(stationName);
        radioStation.setSource(stationSource);
        radioStation.setPlay(false);
        radioStation.setId(id);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        radioStation.setImage(stream.toByteArray());

        realm.copyToRealm(radioStation);
        realm.commitTransaction();
    }

    private int getNextKey() {
        realm.beginTransaction();
        int id = 0;
        try {
            id = realm.where(RadioStation.class).max("id").intValue() + 1;
        } catch (Exception e) {
            realm.commitTransaction();
            return id;
        }
        realm.commitTransaction();
        return id;
    }

    public RealmResults<RadioStation> getStations() {
        return realm.allObjects(RadioStation.class);
    }

    public void setPlayStation(String source) {
        setAllPlayStationOff();
        realm.beginTransaction();
        RadioStation radioStation = realm
                .where(RadioStation.class)
                .equalTo(RadioStation.getSourceFieldName(), source)
                .findFirst();

        if (radioStation != null) {
            radioStation.setPlay(true);
        }
        realm.commitTransaction();
    }

    public void closeBD() {
        realm.close();
    }

    public void clearBD() {
        realm.beginTransaction();
        realm.clear(RadioStation.class);
        realm.commitTransaction();
    }

    private void setAllPlayStationOff() {
        realm.beginTransaction();

        RealmResults<RadioStation> radioStations = realm.where(RadioStation.class).findAll();
        if (!radioStations.isEmpty()) {
            for (int i = radioStations.size() - 1; i >= 0; i--) {
                radioStations.get(i).setPlay(false);
            }
        }
        realm.commitTransaction();
    }


    public void deleteStation(String source) {
        realm.beginTransaction();
        realm.where(RadioStation.class).equalTo(RadioStation.getSourceFieldName(), source).findFirst().removeFromRealm();
        realm.commitTransaction();
    }
}
