package com.somenameofpackage.internetradiowithmosby.ui;


import android.graphics.Bitmap;

public interface AddStation {
    void addStationToBD(String name, String source, Bitmap icon);
    void openDialogCreateStation();
}
