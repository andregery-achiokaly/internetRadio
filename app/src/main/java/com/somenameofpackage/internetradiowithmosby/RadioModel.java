package com.somenameofpackage.internetradiowithmosby;


import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private RadioListener radioListener;
    private boolean play = false;

    void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                radioListener.onPause("I'm stop");
                play = false;
            }
        }
    }

    void startPlay() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            play = true;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (play) mp.start();
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
                play = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setRadioListener(RadioListener radioListener) {
        this.radioListener = radioListener;
    }

    void setSource(String source) {
        if(mediaPlayer == null) {
            createMediaPlayer(source);
        }
    }
}
