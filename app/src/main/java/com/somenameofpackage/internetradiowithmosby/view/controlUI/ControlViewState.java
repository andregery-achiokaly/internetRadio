package com.somenameofpackage.internetradiowithmosby.view.controlUI;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

class ControlViewState implements ViewState<RadioView> {
    private Status status;

    @Override
    public void apply(RadioView view, boolean retained) {
        view.showStatus(status);
    }

    void setStatus(Status status) {
        this.status = status;
    }
}
