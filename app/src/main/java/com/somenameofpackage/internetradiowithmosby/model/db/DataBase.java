package com.somenameofpackage.internetradiowithmosby.model.db;


import android.content.Context;

import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

public interface DataBase {
    Observable<Station> getPlayingStationSource();
    void addStation(Station station);
    Observable<RealmResults<Station>> getStations();
    void setPlayStation(String source);
    void closeBD();
    void deleteStation(String source);
    void init(Context context);
}
