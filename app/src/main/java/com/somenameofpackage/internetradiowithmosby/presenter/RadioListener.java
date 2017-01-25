package com.somenameofpackage.internetradiowithmosby.presenter;


public interface RadioListener {
    void onPlay();
    void onPause();
    void onWait();
    void onError(String message);
}
