package com.somenameofpackage.internetradiowithmosby.presenter;


public interface RadioListener {
    void onPlay(String message);
    void onPause(String message);
    void onError(String message);
}
