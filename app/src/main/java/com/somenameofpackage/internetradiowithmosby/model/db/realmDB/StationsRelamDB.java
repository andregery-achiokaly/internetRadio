package com.somenameofpackage.internetradiowithmosby.model.db.realmDB;

import android.content.Context;
import android.content.SharedPreferences;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

import static android.content.Context.MODE_PRIVATE;

public class StationsRelamDB implements DataBase {
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

    public Observable<Station> getPlayingStationSource() {
        Station station = realm.where(Station.class)
                .equalTo(Station.STATION_IS_PLAY, true)
                .findFirstAsync();

        if (station == null) station = realm.where(Station.class).findFirstAsync();
        return station.asObservable();
    }

    public void addStation(Station station) {
        addStation(station.getName(), station.getSource());
    }

    @Override
    public void setPlayingStationSource(Station station) {
        if (station == null) return;
        realm.executeTransactionAsync(realm1 -> {
            realm1.where(Station.class)
                    .equalTo(Station.STATION_IS_PLAY, true)
                    .findFirst()
                    .setPlay(false);

            realm1.where(Station.class)
                    .equalTo(Station.STATION_ID_KEY, station.getId_key())
                    .findFirst()
                    .setPlay(true);
        });
    }

    private void addStation(String stationName, String stationSource) {
        realm.executeTransactionAsync(realm1 -> {
            Station station = new Station();
            int newID = 0;
            Number number = realm1.where(Station.class).max(Station.STATION_ID_KEY);
            if (number != null) newID = number.intValue();

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
