package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.Context;

import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;

public class StationsRelamDB implements DataBase {
    private final Realm realm;
    private static final String nameOfConfiguration = "Config2";

    public StationsRelamDB(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(nameOfConfiguration)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
    }

    public String getPlayingStationSource() {
        realm.beginTransaction();
        Station station = realm
                .where(Station.class)
                .equalTo(Station.getIsPlayFieldName(), true)
                .findFirst();

        if (station == null) {
            station = realm.where(Station.class).findFirst();
        }
        realm.commitTransaction();
        if (station == null) return null;
        return station.getSource();
    }

    public void addStation(Station station) {
        addStation(station.getName(), station.getSource());
    }

    private void addStation(String stationName, String stationSource) {
        realm.beginTransaction();
        Station station = new Station();
        int newID = 0;
        Number number = realm.where(Station.class).max(Station.getsetId_keyFieldName());
        if (number != null) newID = number.intValue();

        station.setId_key(newID + 1);
        station.setName(stationName);
        station.setSource(stationSource);
        station.setPlay(false);

        realm.copyToRealm(station);
        realm.commitTransaction();
    }

    public Observable<RealmResults<Station>> getStations() {
        realm.beginTransaction();
        Observable<RealmResults<Station>> stations = realm.allObjects(Station.class).asObservable();
        realm.commitTransaction();
        return stations;
    }

    public void setPlayStation(String source) {
        setAllPlayStationOff();
        realm.beginTransaction();
        Station station = realm
                .where(Station.class)
                .equalTo(Station.getSourceFieldName(), source)
                .findFirst();

        if (station != null) {
            station.setPlay(true);
        }
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

    public void deleteStation(String source) {
        realm.beginTransaction();
        realm.where(Station.class).equalTo(Station.getSourceFieldName(), source).findFirst().removeFromRealm();
        realm.commitTransaction();
    }
}
