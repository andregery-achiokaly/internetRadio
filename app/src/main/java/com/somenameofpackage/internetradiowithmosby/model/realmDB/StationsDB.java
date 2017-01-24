package com.somenameofpackage.internetradiowithmosby.model.realmDB;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StationsDB {
    private Realm realm;

    public StationsDB(Context context) {
        RealmConfiguration config2 = new RealmConfiguration.Builder(context)
                .name("default2")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config2);
    }

    public void addStation(String stationName, String stationSource, Bitmap icon){
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

    public void deleteStation(){

    }

    public void getStation(){

    }

    public RealmResults<Station> getStations(){
        return realm.allObjects(Station.class);
    }

    public void changeStation(){

    }

    public void closeBD() {
        realm.close();
    }

    public void clearBD(){
        realm.beginTransaction();
        realm.clear(Station.class);
        realm.commitTransaction();
    }
}
