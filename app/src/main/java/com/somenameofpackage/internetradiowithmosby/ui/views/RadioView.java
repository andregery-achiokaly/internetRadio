package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

public interface RadioView extends MvpView{
    void showStatus(Status status);
}
