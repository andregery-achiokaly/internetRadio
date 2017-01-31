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
import com.somenameofpackage.internetradiowithmosby.presenter.RadioPresenter;
import com.somenameofpackage.internetradiowithmosby.ui.viewStates.ControlViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlFragment extends MvpViewStateFragment<RadioView, RadioPresenter> implements RadioView {
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
    public RadioPresenter createPresenter() {
        return new RadioPresenter(getContext());
    }

    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.buttonPressed(getContext());
    }

    @Override
    public void showStatus(Status status) {
        ControlViewState controlViewState = (ControlViewState) viewState;
        controlViewState.setStatus(status);
        switch (status){
            case Stop: playButton.setText(Status.Play.name()); break;
            case Play: playButton.setText(Status.Stop.name()); break;
            case Error: playButton.setText(Status.Error.name()); break;
        }
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new ControlViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showStatus(Status.Stop);
    }
}
