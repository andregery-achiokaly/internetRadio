package com.somenameofpackage.internetradiowithmosby.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.ControlPresenter;
import com.somenameofpackage.internetradiowithmosby.ui.viewStates.ControlViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlFragment extends MvpViewStateFragment<RadioView, ControlPresenter> implements RadioView {
    @BindView(R.id.play_btn)
    Button playButton;

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
    public ControlPresenter createPresenter() {
        return new ControlPresenter(getContext());
    }

    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.changePlayState();
    }

    @Override
    public void showStatus(Status status) {
        ControlViewState controlViewState = (ControlViewState) viewState;
        controlViewState.setStatus(status);
        playButton.setText(status.toString());
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new ControlViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showStatus(Status.isStop);
    }
}
