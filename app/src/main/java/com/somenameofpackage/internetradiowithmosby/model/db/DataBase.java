package com.somenameofpackage.internetradiowithmosby.model.db;


import java.util.List;

public interface DataBase {
    String getPlayingStationSource();
    void addStation(Station station);
    List<Station> getStations();
    void setPlayStation(String source);
    void closeBD();
    void clearBD();
    void deleteStation(String source);
}
