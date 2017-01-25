package com.somenameofpackage.internetradiowithmosby.model.realmDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StationsDB {
    private Realm realm;
    private static final String nameOfConfiguration = "default2";

    public StationsDB(Context context) {
        RealmConfiguration config2 = new RealmConfiguration.Builder(context)
                .name(nameOfConfiguration)
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config2);
    }

    public Station getPlaying() {
        realm.beginTransaction();
        Station station = realm
                .where(Station.class)
                .equalTo(Station.getIsPlayFieldName(), true)
                .findFirst();

        if (station == null) {
            station = realm.where(Station.class).findFirst();
        }
        realm.commitTransaction();
        return station;
    }

    public int getNumberOfPlayingStation() {
        realm.beginTransaction();
        RealmResults<Station> stations = realm.where(Station.class).findAll();
        int id = 0;
        if (!stations.isEmpty()) {
            for (int i = 0; i < stations.size(); i++) {
                Log.v("GGG", "s: " + stations.get(i).getName());
                if (stations.get(i).isPlay()) break;
                id++;
            }
        }
        realm.commitTransaction();
        return id;
    }

    public void addStation(String stationName, String stationSource, Bitmap icon) {
        realm.beginTransaction();
        Station station = realm.createObject(Station.class);
        station.setName(stationName);
        station.setSource(stationSource);
        station.setPlay(false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        station.setImage(stream.toByteArray());
        realm.commitTransaction();
    }

    public RealmResults<Station> getStations() {
        return realm.allObjects(Station.class);
    }

    public void setPlayStation(String source) {
        setAllPlayStationOff();
        realm.beginTransaction();
        Station station = realm
                .where(Station.class)
                .equalTo(Station.getSourceFieldName(), source)
                .findFirst();

        if (station != null) station.setPlay(true);
        realm.commitTransaction();
    }

    public void closeBD() {
        realm.close();
    }

    public void clearBD() {
        realm.beginTransaction();
        realm.clear(Station.class);
        realm.commitTransaction();
    }

    private void setAllPlayStationOff() {
        realm.beginTransaction();

        RealmResults<Station> stations = realm.where(Station.class).findAll();
        if (!stations.isEmpty()) {
            for (int i = stations.size() - 1; i >= 0; i--) {
                stations.get(i).setPlay(false);
            }
        }
        realm.commitTransaction();
    }
}
