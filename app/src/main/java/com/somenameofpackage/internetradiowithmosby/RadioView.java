package com.somenameofpackage.internetradiowithmosby;


import com.hannesdorfmann.mosby.mvp.MvpView;

interface RadioView extends MvpView{
    void showMessage(String message);
}
