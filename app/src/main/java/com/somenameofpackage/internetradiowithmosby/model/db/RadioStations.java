package com.somenameofpackage.internetradiowithmosby.model.db;

import android.content.Context;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;

import io.realm.RealmResults;
import rx.Observable;

public class RadioStations {
    private final DataBase dataBase;

    public RadioStations(Context context) {
        dataBase = new StationsRelamDB(context);
    }

    public void firstInitial(Context context) {
        addStation(context.getString(R.string.best_fm_name),
                context.getString(R.string.best_fm_source));

        addStation(context.getString(R.string.jam_fm_name),
                context.getString(R.string.jam_fm_source));
    }

    public rx.Observable<RealmResults<Station>> getStationsObservable() {
        return dataBase.getStations();
    }

    public void addStation(String name, String source) {
        dataBase.addStation(new Station(name, source));
    }

    public void removeStation(Station station) {
        dataBase.deleteStation(station.getSource());
    }

    public void removeStation(String source) {
        dataBase.deleteStation(source);
    }

    public Observable<String> getPlayingStationSource() {
        return Observable.create(emitter -> {
            emitter.onNext(dataBase.getPlayingStationSource());
            emitter.onCompleted();
        });
    }

    public void setPlayStation(String source) {
        if (source != null)
            dataBase.setPlayStation(source);
    }

    public void closeBD() {
        dataBase.closeBD();
    }
}
