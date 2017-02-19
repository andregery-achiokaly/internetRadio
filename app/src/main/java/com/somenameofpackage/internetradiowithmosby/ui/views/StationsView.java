package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;

import io.realm.OrderedRealmCollection;

public interface StationsView extends MvpView {
    void showCurrentStation(String source);
    void setListStations(OrderedRealmCollection<Station> value);
}
