package com.somenameofpackage.internetradiowithmosby;


import com.hannesdorfmann.mosby.mvp.MvpView;

interface RadioView extends MvpView{
    void showPlay(String message);
    void showPause(String message);
}
