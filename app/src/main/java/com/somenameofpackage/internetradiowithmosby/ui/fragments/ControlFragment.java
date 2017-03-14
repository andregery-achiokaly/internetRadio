package com.somenameofpackage.internetradiowithmosby.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.ControlPresenter;
import com.somenameofpackage.internetradiowithmosby.ui.viewStates.ControlViewState;
import com.somenameofpackage.internetradiowithmosby.ui.views.ControlView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlFragment extends MvpViewStateFragment<ControlView, ControlPresenter> implements ControlView {
    @BindView(R.id.play_btn)
    ImageButton playButton;

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    public ControlFragment() {
    }

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
        playButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_play_arrow_black_24dp));
        return view;
    }

    @NonNull
    @Override
    public ControlPresenter createPresenter() {
        return new ControlPresenter(getActivity().getApplicationContext());
    }

    @OnClick(R.id.play_btn)
    public void onPlayClicked() {
        presenter.changePlayState();
    }

    @Override
    public void showStatus(RadioStatus radioStatus) {
        ControlViewState controlViewState = (ControlViewState) viewState;
        if (controlViewState != null) controlViewState.setRadioStatus(radioStatus);
        switch (radioStatus) {
            case isPlay:
                playButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_pause_black_24dp));
                break;
            case isStop:
                playButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_play_arrow_black_24dp));
                break;
            case Error:
                playButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_error_black_24dp));
                break;
            case Wait:
                playButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_history_black_24dp));
                break;
        }
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new ControlViewState();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause(getActivity().getApplicationContext());
    }

    @Override
    public void onNewViewStateInstance() {
        showStatus(RadioStatus.isStop);
    }
}
