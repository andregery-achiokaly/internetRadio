package com.somenameofpackage.internetradiowithmosby.model.db;


import android.content.Context;

import io.realm.RealmResults;
import rx.Observable;

public interface DataBase {
    Observable<Station> getCurrentStation();
    void addStation(Station station);
    void setPlayingStationSource(Station station);
    Observable<RealmResults<Station>> getStations();
    void closeBD();
    void deleteStation(String source);
    void setDefaultValues(Context context);
}
