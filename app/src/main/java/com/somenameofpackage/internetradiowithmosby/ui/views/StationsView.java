package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import java.util.List;

public interface StationsView extends MvpView {
    void setListStations(List<Station> value);
    void setAdapter(List<Station> data);
}
