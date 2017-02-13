package com.somenameofpackage.internetradiowithmosby.model.db;


import java.util.List;

import io.realm.RealmResults;
import rx.Observable;

public interface DataBase {
    String getPlayingStationSource();
    void addStation(Station station);
    Observable<RealmResults<Station>> getStations();
    void setPlayStation(String source);
    void closeBD();
    void deleteStation(String source);
}
