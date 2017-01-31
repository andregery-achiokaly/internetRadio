package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface StationsView extends MvpView {
    void showCurrentStation(int id);
    void disableAllStation();
}
