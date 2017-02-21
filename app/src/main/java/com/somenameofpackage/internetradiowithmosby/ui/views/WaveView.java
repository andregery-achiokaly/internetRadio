package com.somenameofpackage.internetradiowithmosby.ui.views;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface WaveView extends MvpView {
    void updateVisualizer(byte[] bytes);
}
