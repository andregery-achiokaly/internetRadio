package com.somenameofpackage.internetradiowithmosby.model.radio;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.presenter.RadioListener;

import java.io.IOException;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private RadioListener radioListener;
    private String currentSource = "";

    public void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                radioListener.onPause("I'm stop");
            }
        }
    }

    public void startPlay(String source) {
        if (mediaPlayer != null) {
            if (currentSource.equals(source)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {
                closeMediaPlayer();
                createMediaPlayer(source);
                currentSource = source;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        radioListener.onPlay("I'm playing");
    }

    private void createMediaPlayer(String source) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            radioListener.onError("RadioModel. Oh no... Something went wrong :(");
        }
    }

    void closeMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setRadioListener(RadioListener radioListener) {
        this.radioListener = radioListener;
    }

    void setSource(String source) {
        if (mediaPlayer == null) {
            createMediaPlayer(source);
        }
    }
}
