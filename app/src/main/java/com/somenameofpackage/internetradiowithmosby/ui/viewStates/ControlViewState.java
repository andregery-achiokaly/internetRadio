package com.somenameofpackage.internetradiowithmosby.ui.viewStates;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.RadioStatus;
import com.somenameofpackage.internetradiowithmosby.ui.views.ControlView;

public class ControlViewState implements ViewState<ControlView> {
    private RadioStatus radioStatus;

    @Override
    public void apply(ControlView view, boolean retained) {
        view.showStatus(radioStatus);
    }

    public void setRadioStatus(RadioStatus radioStatus) {
        this.radioStatus = radioStatus;
    }
}
