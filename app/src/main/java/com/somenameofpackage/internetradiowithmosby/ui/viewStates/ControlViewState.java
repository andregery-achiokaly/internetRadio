package com.somenameofpackage.internetradiowithmosby.ui.viewStates;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;

public class ControlViewState implements ViewState<RadioView> {
    private Status status;

    @Override
    public void apply(RadioView view, boolean retained) {
        view.showStatus(status);
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
