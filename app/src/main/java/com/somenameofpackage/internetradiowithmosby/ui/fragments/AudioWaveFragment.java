package com.somenameofpackage.internetradiowithmosby.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class AudioWaveFragment extends MvpFragment<WaveView, AudioWavePresenter> implements WaveView {
    public static final int WAVE_VIEW_RECORD_AUDIO_PERMISSION = 4115;
    @BindView(R.id.audioWave)
    AudioWaveView audioWaveView;

    public static AudioWaveFragment newInstance() {
        AudioWaveFragment fragment = new AudioWaveFragment();
        return fragment;
    }

    public AudioWaveFragment() {
    }

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
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                presenter.setCanShow(false);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        WAVE_VIEW_RECORD_AUDIO_PERMISSION);
                presenter.setCanShow(false);
            }
        } else {
            presenter.setCanShow(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause(getActivity().getApplicationContext());
    }

    @Override
    public void updateVisualizer(byte[] bytes) {
        audioWaveView.updateVisualizer(bytes);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WAVE_VIEW_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.setCanShow(true);
            } else {
                presenter.setCanShow(false);
            }
        }
    }
}
