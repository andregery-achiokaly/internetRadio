package com.somenameofpackage.internetradiowithmosby.model.db;


import android.graphics.Bitmap;

import com.somenameofpackage.internetradiowithmosby.model.realmDB.RadioStation;
import java.util.List;

public interface DataBase {
    String getPlayingSource();
    int getNumberOfPlayingStation();
    void addStation(String stationName, String stationSource, Bitmap icon);
    List<RadioStation> getStations();
    void setPlayStation(String source);
    void closeBD();
    void clearBD();
    void deleteStation(String source);
}
