package com.somenameofpackage.internetradiowithmosby.ui.viewStates;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.ControlView;

public class ControlViewState implements ViewState<ControlView> {
    private Status status;

    @Override
    public void apply(ControlView view, boolean retained) {
        view.showStatus(status);
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
