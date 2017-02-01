package com.somenameofpackage.internetradiowithmosby.model.db;


import android.graphics.Bitmap;

import java.util.List;

public interface DataBase {
    String getPlayingStationSource();
    void addStation(RadioStation radioStation);
    List<RadioStation> getStations();
    void setPlayStation(String source);
    void closeBD();
    void clearBD();
    void deleteStation(String source);
}
