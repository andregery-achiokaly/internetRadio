package com.somenameofpackage.internetradiowithmosby.view.audioWave;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface WaveView extends MvpView {
    void updateVisualizer(byte[] bytes);
}
