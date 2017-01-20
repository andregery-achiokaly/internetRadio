package com.somenameofpackage.internetradiowithmosby;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RadioActivity extends MvpActivity<RadioView, RadioPresenter> implements RadioView {
    final String DATA_STREAM = "http://online.radiorecord.ru:8101/rr_128";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public RadioPresenter createPresenter() {
        return new RadioPresenter();
    }

    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.startPlaying(DATA_STREAM);
    }

    @OnClick(R.id.stop_btn)
    public void onStopClicked() {
        presenter.stopPlaying();
    }

    @Override
    public void showPlay() {

    }

    @Override
    public void showPause() {

    }
}
