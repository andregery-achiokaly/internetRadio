package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";

    private PublishSubject<String> subscriber = PublishSubject.create();

    public Subject<String> getRadioModelObservable() {
        return subscriber;
    }

    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                subscriber.onNext("");
            }
        }
    }

    private void startPlay(String source) {
        if (mediaPlayer != null && source != null) {
            if (currentSource.equals(source)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    subscriber.onNext(currentSource);

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
        subscriber.onNext(currentSource);
        subscriber.onNext("");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            if (!mp.isPlaying()) mp.start();
        }
        subscriber.onNext(currentSource);
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
            subscriber.onError(e);
        } catch (NullPointerException e) {
            currentSource = "";
            subscriber.onError(e);
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

    void setSource(String source) {
        if (mediaPlayer == null) {
            createMediaPlayer(source);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
