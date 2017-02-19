package com.somenameofpackage.internetradiowithmosby.model.db;


import android.content.Context;

import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public interface DataBase {
    Observable<Station> getPlayingStationSource();
    void addStation(Station station);
    Observable<RealmResults<Station>> getStations();
    void closeBD();
    void deleteStation(String source);
    void setDefaultValues(Context context);
}
