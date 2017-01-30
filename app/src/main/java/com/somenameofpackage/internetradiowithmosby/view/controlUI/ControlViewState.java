package com.somenameofpackage.internetradiowithmosby.view.controlUI;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class ControlViewState implements ViewState<RadioView> {
    String message;

    @Override
    public void apply(RadioView view, boolean retained) {
        view.showStatus(message);
    }

    public void setStatus(String message) {
        this.message = message;
    }
}
