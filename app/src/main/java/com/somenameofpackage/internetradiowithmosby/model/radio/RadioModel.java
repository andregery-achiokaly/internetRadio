package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;

import java.io.IOException;
import java.util.LinkedList;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private final static String CRASH = "Radio crashed";
    private final static String LIST_OF_RADIO_STATIONS_IS_EMPTY = "List of radio stations is empty";

    private MediaPlayer mediaPlayer;
    private final LinkedList<RadioListener> listOfRadioListener = new LinkedList<>();
    private String currentSource = "";

    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                for (RadioListener r : listOfRadioListener) {
                    if (r != null) r.onPause();
                }
            }
        }
    }

    private void startPlay(String source) {
        if (mediaPlayer != null && source != null) {
            if (currentSource.equals(source)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    for (RadioListener r : listOfRadioListener) {
                        if (r != null) r.onPlay(currentSource);
                    }
                }
            } else {
                mediaPlayer.release();
                mediaPlayer = null;
                createMediaPlayer(source);
                currentSource = source;
            }
        }
    }

    public void changePlayState(String source) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() && currentSource.equals(source)) {
                stopPlay();
            } else startPlay(source);
        }
    }

    void changePlayState() {
        changePlayState(currentSource);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        for (RadioListener r : listOfRadioListener) {
            if (r != null) {
                r.onPlay(currentSource);
                r.onError("Error");
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            if (!mp.isPlaying()) mp.start();
        }
        for (RadioListener r : listOfRadioListener) if (r != null) r.onPlay(currentSource);
    }

    private void createMediaPlayer(String source) {
        try {
            if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
            else mediaPlayer.release();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            for (RadioListener r : listOfRadioListener)
                if (r != null) r.onError(CRASH);
        } catch (NullPointerException e) {
            currentSource = "";
            for (RadioListener r : listOfRadioListener)
                if (r != null) r.onError(LIST_OF_RADIO_STATIONS_IS_EMPTY);
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
