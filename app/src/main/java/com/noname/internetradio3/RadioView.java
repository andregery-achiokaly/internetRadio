package com.noname.internetradio3;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface RadioView extends MvpView{

    void startPlay();
    void stopPlay();
}
