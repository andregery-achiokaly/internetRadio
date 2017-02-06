package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import java.util.List;

public interface StationsView extends MvpView {
    void showCurrentStation(String source);
    void disableAllStation();
    void setListStation(List<Station> value);

    void onChange();
}
