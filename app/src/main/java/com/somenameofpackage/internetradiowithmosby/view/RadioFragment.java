package com.somenameofpackage.internetradiowithmosby.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.RadioPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RadioFragment extends MvpFragment<RadioView, RadioPresenter> implements RadioView {
    final String SOURCE = "http://cast.radiogroup.com.ua:8000/europaplus";

    @BindView(R.id.message)
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    @Override
    public RadioPresenter createPresenter() {
        return new RadioPresenter();
    }


    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.startPlaying(SOURCE, getContext());
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
