package com.somenameofpackage.internetradiowithmosby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RadioActivity extends MvpActivity<RadioView, RadioPresenter> implements RadioView {
    final String SOURCE = "http://cast.radiogroup.com.ua:8000/europaplus";

    @BindView(R.id.message)
    TextView textView;

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
        presenter.startPlaying(SOURCE, getApplicationContext());
    }

    @OnClick(R.id.stop_btn)
    public void onStopClicked() {
        presenter.stopPlaying();
    }

    @Override
    public void showMessage(String message) {
        textView.setText(message);
    }
}
