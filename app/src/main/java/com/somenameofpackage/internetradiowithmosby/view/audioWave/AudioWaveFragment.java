package com.somenameofpackage.internetradiowithmosby.view.audioWave;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.presenter.AudioWavePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioWaveFragment extends MvpFragment<WaveView, AudioWavePresenter> implements WaveView{
    @BindView(R.id.audioWave)
    AudioWaveView audioWaveView;

    @Override
    public AudioWavePresenter createPresenter() {
        return new AudioWavePresenter(getContext());
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
    public void updateVisualizer(byte[] bytes){
        audioWaveView.updateVisualizer(bytes);
    }
}
