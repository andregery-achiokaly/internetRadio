package com.somenameofpackage.internetradiowithmosby.view.radioList;


import android.util.Log;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class StationsListViewState implements ViewState<StationsView> {
    private int currentStation;
    @Override
    public void apply(StationsView view, boolean retained) {
        view.showCurrentStation(currentStation);
    }

    public void setCurrentStation(int currentStation) {
        this.currentStation = currentStation;
    }
}
