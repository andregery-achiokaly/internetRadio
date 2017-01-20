package com.somenameofpackage.internetradiowithmosby;


import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private RadioListener radioListener;

    void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                radioListener.onPause("I'm stop");
            }
        }
    }

    void startPlay(String source) {
        if (mediaPlayer == null) createMediaPlayer(source);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //show wait dialog
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (mp.isPlaying()) {
            radioListener.onPlay("I'm playing");
        }
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
            radioListener.onError("Oh no... Something went wrong :(");
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
        createMediaPlayer(source);
    }
}
