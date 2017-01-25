package com.somenameofpackage.internetradiowithmosby.presenter;


public interface RadioListener {
    void onPlay();
    void onPause();
    void onError(String message);
}
