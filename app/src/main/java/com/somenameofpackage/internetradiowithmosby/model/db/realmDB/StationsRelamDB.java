package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.Context;
import android.content.SharedPreferences;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;

import static android.content.Context.MODE_PRIVATE;

public class StationsRelamDB implements DataBase {
    private Realm realm;
    private static final String nameOfConfiguration = "Config";
    final private static String INITIAL_DB = "IS_INITIAL_DB";
    final private static String REALM_DB_PREFERENCES = "REALM_DB_PREFERENCES";

    public void init(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name(nameOfConfiguration)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
        createBD(context);
    }

    private void createBD(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REALM_DB_PREFERENCES, MODE_PRIVATE);
        Boolean isCreated = sharedPreferences.getBoolean(INITIAL_DB, false);
        if (!isCreated) {
            firstInitial(context);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean(INITIAL_DB, true);
            ed.apply();
        }
    }

    private void firstInitial(Context context) {
        addStation(context.getString(R.string.best_fm_name),
                context.getString(R.string.best_fm_source));

        addStation(context.getString(R.string.jam_fm_name),
                context.getString(R.string.jam_fm_source));
    }

    public Observable<Station> getPlayingStationSource() {
        Station station = realm
                .where(Station.class)
                .equalTo(Station.getIsPlayFieldName(), true)
                .findFirst();

        if (station == null) station = realm.where(Station.class).findFirst();
        return station.asObservable();
    }

    public void addStation(Station station) {
        addStation(station.getName(), station.getSource());
    }

    private void addStation(String stationName, String stationSource) {
        realm.beginTransaction();
        Station station = new Station();
        int newID = 0;
        Number number = realm.where(Station.class).max(Station.getId_keyFieldName());
        if (number != null) newID = number.intValue();

        station.setId_key(newID + 1);
        station.setName(stationName);
        station.setSource(stationSource);
        station.setPlay(false);

        realm.copyToRealm(station);
        realm.commitTransaction();
    }

    public Observable<RealmResults<Station>> getStations() {
        return realm.allObjects(Station.class).asObservable();
    }

    public void setPlayStation(String source) {
        if (source == null) return;
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
