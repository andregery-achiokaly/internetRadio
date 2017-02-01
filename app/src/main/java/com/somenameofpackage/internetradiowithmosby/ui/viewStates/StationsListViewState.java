package com.somenameofpackage.internetradiowithmosby.ui.viewStates;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

public class StationsListViewState implements ViewState<StationsView> {
    private String station;
    @Override
    public void apply(StationsView view, boolean retained) {
        view.showCurrentStation(station);
    }

    public void setCurrentStation(String station) {
        this.station = station;
    }
}
