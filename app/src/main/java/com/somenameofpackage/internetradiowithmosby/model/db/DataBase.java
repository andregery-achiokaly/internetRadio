package com.somenameofpackage.internetradiowithmosby.model.db;


import io.realm.RealmResults;

public interface DataBase {
    String getPlayingStationSource();
    void addStation(Station station);
    RealmResults<Station> getStations();
    void setPlayStation(String source);
    void closeBD();
    void clearBD();
    void deleteStation(String source);
}
