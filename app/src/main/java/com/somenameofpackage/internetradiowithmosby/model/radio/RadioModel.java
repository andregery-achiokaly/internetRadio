package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import java.io.IOException;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";

    private PublishSubject<Status> subscriber = PublishSubject.create();

    public Subject<Status, Status> getRadioModelObservable() {
        return subscriber;
    }

    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                subscriber.onNext(Status.isStop);
            }
        }
    }

    private void startPlay(String source) {
        if (mediaPlayer != null && source != null) {
            if (currentSource.equals(source)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    subscriber.onNext(Status.isPlay);
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
        subscriber.onNext(Status.Wait);
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
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            if (!mp.isPlaying()) mp.start();
        }
        subscriber.onNext(Status.isPlay);
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
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.start();
        } catch (IOException | NullPointerException e) {
            currentSource = "";
            subscriber.onNext(Status.Error);
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == 1 || extra == 1) subscriber.onNext(Status.Error);
        return false;
    }
}
