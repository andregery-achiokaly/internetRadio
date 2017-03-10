package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.RadioStatus;

public interface ControlView extends MvpView{
    void showStatus(RadioStatus radioStatus);
}
