package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import java.io.IOException;

import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class RadioModel implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";

    private PublishSubject<Status> statusSubscriber = PublishSubject.create();
    private ReplaySubject<Integer> radioIdSubscriber = ReplaySubject.createWithSize(1);

    PublishSubject<Status> getRadioModelStatusObservable() {
        return statusSubscriber;
    }

    ReplaySubject<Integer> getRadioIdObservable() {
        return radioIdSubscriber;
    }

    private void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                statusSubscriber.onNext(Status.isStop);
            }
        }
    }

    private void startPlay(String source) {
        if (mediaPlayer != null && source != null) {
            if (currentSource.equals(source)) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    statusSubscriber.onNext(Status.isPlay);
                }
            } else {
                mediaPlayer.release();
                mediaPlayer = null;
                createMediaPlayer(source);
                currentSource = source;
            }
        }
    }

    private void changePlayState(String source) {
        if (source == null) source = currentSource;
        statusSubscriber.onNext(Status.Wait);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() && currentSource.equals(source)) {
                stopPlay();
            } else startPlay(source);
        } else {
            createMediaPlayer(source);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            if (!mp.isPlaying()) mp.start();
        }
        statusSubscriber.onNext(Status.isPlay);
    }

    private void createMediaPlayer(String source) {
        try {
            if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
            else mediaPlayer.release();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.start();
            radioIdSubscriber.onNext(mediaPlayer.getAudioSessionId());
        } catch (IOException | NullPointerException e) {
            currentSource = "";
            statusSubscriber.onNext(Status.Error);
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == 1 || extra == 1) statusSubscriber.onNext(Status.Error);
        return false;
    }

    void setChangePlaySubject(PublishSubject<String> changeStateSubject) {
        changeStateSubject.subscribe(this::changePlayState);
    }
}
