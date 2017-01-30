package com.somenameofpackage.internetradiowithmosby.model.radio;


import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.presenter.RadioListener;

import java.io.IOException;
import java.util.LinkedList;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private LinkedList<RadioListener> listOfRadioListener = new LinkedList<>();
    private String currentSource = "";
    private boolean isPlay;

    public void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                for (RadioListener r : listOfRadioListener) {
                    if (r != null) r.onPause();
                }
            }
        }
        isPlay = false;
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
            for (RadioListener r : listOfRadioListener) {
                if (r != null) r.onPlay();
            }
        }
        isPlay = true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (isPlay) mp.start();
        for (RadioListener r : listOfRadioListener) if (r != null) r.onPlay();
    }

    private void createMediaPlayer(String source) {
        isPlay = false;
        try {
            if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
            else mediaPlayer.release();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            for (RadioListener r : listOfRadioListener)
                if (r != null) r.onError("RadioModel crash :(");
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

    void addRadioListener(RadioListener radioListener) {
        this.listOfRadioListener.add(radioListener);
    }

    void setSource(String source) {
        if (mediaPlayer == null) {
            createMediaPlayer(source);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
