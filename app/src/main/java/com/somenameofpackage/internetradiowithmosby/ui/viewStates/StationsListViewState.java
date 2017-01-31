package com.somenameofpackage.internetradiowithmosby.ui.viewStates;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

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
