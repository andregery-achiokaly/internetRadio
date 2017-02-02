package com.somenameofpackage.internetradiowithmosby.presenter.listeners;


public interface RadioListener {
    void onPlay();
    void onPause();
    void onError(String message);
}
