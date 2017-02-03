package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.Context;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStation;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StationsRelamDB implements DataBase{
    private final Realm realm;
    private static final String nameOfConfiguration = "Configuration1";

    public StationsRelamDB(Context context) {

        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(nameOfConfiguration)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
    }

    public String getPlayingStationSource() {
        realm.beginTransaction();
        RadioStation radioStation = realm
                .where(RadioStation.class)
                .equalTo(RadioStation.getIsPlayFieldName(), true)
                .findFirst();

        if (radioStation == null) {
            radioStation = realm.where(RadioStation.class).findFirst();
        }
        realm.commitTransaction();
        if(radioStation == null) return null;
        return radioStation.getSource();
    }

    public void addStation(RadioStation radioStation) {
        addStation(radioStation.getName(), radioStation.getSource(), radioStation.getImage());
    }

    private void addStation(String stationName, String stationSource, byte[] icon) {
        realm.beginTransaction();
        RadioStation radioStation = new RadioStation();
        radioStation.setName(stationName);
        radioStation.setSource(stationSource);
        radioStation.setPlay(false);
        radioStation.setImage(icon);

        realm.copyToRealm(radioStation);
        realm.commitTransaction();
    }

    public RealmResults<RadioStation> getStations() {
        realm.beginTransaction();
        RealmResults<RadioStation> radioStations = realm.allObjects(RadioStation.class);
        realm.commitTransaction();
        return radioStations;
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
