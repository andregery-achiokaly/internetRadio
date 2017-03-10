package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import com.somenameofpackage.internetradiowithmosby.ui.fragments.RadioStatus;

import java.io.IOException;

import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

public class Radio implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";
    private PublishSubject<RadioStatus> statusObserver = PublishSubject.create();
    private ReplaySubject<Integer> radioIdObserver = ReplaySubject.createWithSize(1);
    private Handler radioCloseHandler = new Handler();
    private Runnable radioCloseRunnable = this::closeMediaPlayer;

    PublishSubject<RadioStatus> getRadioModelStatusObservable() {
        return statusObserver;
    }

    ReplaySubject<Integer> getRadioIdObservable() {
        return radioIdObserver;
    }

    private void initPlayer(String source) {
        try {
            statusObserver.onNext(RadioStatus.Wait);
            currentSource = source;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
            radioIdObserver.onNext(mediaPlayer.getAudioSessionId());
        } catch (IOException | NullPointerException e) {
            errorProcessing(e);
        }
    }

    private void errorProcessing(Exception e) {
        e.printStackTrace();
        currentSource = "";
        closeMediaPlayer();
        statusObserver.onNext(RadioStatus.Error);
    }

    private void changeSource(String source) {
        try {
            statusObserver.onNext(RadioStatus.Wait);
            currentSource = source;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            errorProcessing(e);
        }
    }

    void setChangePlaySubject(Subject<String, String> changeStateSubject) {
        changeStateSubject.subscribe(new Subscriber<String>() {
                                         @Override
                                         public void onCompleted() {
                                         }

                                         @Override
                                         public void onError(Throwable e) {
                                             e.printStackTrace();
                                             statusObserver.onNext(RadioStatus.Error);
                                         }

                                         @Override
                                         public void onNext(String source) {
                                             if (mediaPlayer == null) {
                                                 initPlayer(source);
                                             } else {
                                                 changeStateOfCurrentPlayer(source);
                                             }
                                         }
                                     }
        );
    }

    private void changeStateOfCurrentPlayer(String source) {
        if (!source.equals(currentSource)) {
            changeSource(source);
        } else {
            if (mediaPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
        }
    }

    private void play() {
        radioCloseHandler.removeCallbacks(radioCloseRunnable);
        mediaPlayer.start();
        statusObserver.onNext(RadioStatus.isPlay);
    }

    private void pause() {
        mediaPlayer.pause();
        // Wait 1 min. If user doesn't run player again - player will be closed.
        // If we don't close player - player will spend traffic
        radioCloseHandler.postDelayed(radioCloseRunnable, 60 * 1000);
        statusObserver.onNext(RadioStatus.isStop);
    }

    void closeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            statusObserver.onNext(RadioStatus.isStop);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (mp.isPlaying()) statusObserver.onNext(RadioStatus.isPlay);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        statusObserver.onNext(RadioStatus.Error);
        return true;
    }
}
