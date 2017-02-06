package com.somenameofpackage.internetradiowithmosby.presenter.listeners;


public interface RadioListener {
    void onPlay(String source);
    void onPause();
    void onError(String message);
}
