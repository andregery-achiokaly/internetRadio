package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

import static android.content.Context.MODE_PRIVATE;

public class StationsRelamDB {
    @Inject
    Realm realm;
    final private static String INITIAL_DB = "DB_IS_INITIAL";
    final private static String REALM_DB_PREFERENCES = "REALM_DB_PREFERENCES";

    public StationsRelamDB() {
        RadioApplication.getComponent().injectsStationsRelamDB(this);
    }

    public void setDefaultValues(Context context) {
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

    public Observable<Station> getCurrentStation() {
        return realm.where(Station.class)
                .equalTo(Station.STATION_IS_PLAY, true)
                .findFirstAsync().asObservable().filter(s -> s.isValid()).take(1).map(realmObject -> {
                    if (realmObject != null) return (Station) realmObject;
                    else return realm.where(Station.class).findFirstAsync();
                });
    }

    public void addStation(Station station) {
        addStation(station.getName(), station.getSource());
    }

    public void setPlayingStationSource(Station station) {
        if (station == null) return;
        int id = station.getId_key();
        realm.executeTransactionAsync(realm1 -> {
            Station s1 = realm1.where(Station.class)
                    .equalTo(Station.STATION_IS_PLAY, true)
                    .findFirst();
            if (s1 != null) s1.setPlay(false);

            Station s2 = realm1.where(Station.class)
                    .equalTo(Station.STATION_ID_KEY, id)
                    .findFirst();
            if (s2 != null) s2.setPlay(true);
        });
    }

    private void addStation(String stationName, String stationSource) {
        realm.executeTransactionAsync(realm1 -> {
            Station station = new Station();
            int newID = 0;
            Number number = realm1.where(Station.class).max(Station.STATION_ID_KEY);
            if (number != null) newID = number.intValue();
            Log.v("GGG", "TTTT");
            station.setId_key(newID + 1);
            station.setName(stationName);
            station.setSource(stationSource);
            station.setPlay(false);

            realm1.copyToRealm(station);
        });
    }

    public Observable<RealmResults<Station>> getStations() {
        return realm.where(Station.class).findAllAsync().asObservable();
    }

    public void closeBD() {
        realm.close();
    }

    public void clearBD() {
        realm.executeTransactionAsync(realm1 -> realm1.deleteAll());
    }

    public void deleteStation(String source) {
        realm.executeTransactionAsync(realm1 -> realm1.where(Station.class).equalTo(Station.STATION_SOURCE, source)
                .findAll()
                .deleteAllFromRealm());
    }
}
