package com.somenameofpackage.internetradiowithmosby.view.radioList;


import android.graphics.Bitmap;

public interface AddStation {
    void addStationToBD(String name, String source, Bitmap icon);
    void createStationDialog(String title);
}
