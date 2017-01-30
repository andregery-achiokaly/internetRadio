package com.somenameofpackage.internetradiowithmosby.view.controlUI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.RadioPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlFragment extends MvpViewStateFragment<RadioView, RadioPresenter> implements RadioView {
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
        return new RadioPresenter(getContext());
    }


    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.startPlaying(getContext());
    }

    @OnClick(R.id.stop_btn)
    public void onStopClicked() {
        presenter.stopPlaying();
    }

    @Override
    public void showStatus(String status) {
        ControlViewState controlViewState = (ControlViewState) viewState;
        controlViewState.setStatus(status);
        textView.setText(status);
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new ControlViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showStatus("");
    }
}
