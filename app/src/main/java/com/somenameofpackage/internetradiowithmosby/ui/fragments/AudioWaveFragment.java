package com.somenameofpackage.internetradiowithmosby.ui.fragments;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.AudioWavePresenter;
import com.somenameofpackage.internetradiowithmosby.ui.views.AudioWaveView;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioWaveFragment extends MvpFragment<WaveView, AudioWavePresenter> implements WaveView{
    @BindView(R.id.audioWave)
    AudioWaveView audioWaveView;

    @NonNull
    @Override
    public AudioWavePresenter createPresenter() {
        return new AudioWavePresenter(getActivity().getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_wave, container, false);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindService(getActivity().getApplicationContext());
    }

    @Override
    public void updateVisualizer(Byte[] bytes){
        audioWaveView.updateVisualizer(bytes);
    }
}
