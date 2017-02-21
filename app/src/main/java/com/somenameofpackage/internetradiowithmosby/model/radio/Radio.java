package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import java.io.IOException;

import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

public class Radio implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private String currentSource = "";
    private PublishSubject<Status> statusObserver = PublishSubject.create();
    private ReplaySubject<Integer> radioIdObserver = ReplaySubject.createWithSize(1);

    PublishSubject<Status> getRadioModelStatusObservable() {
        return statusObserver;
    }

    ReplaySubject<Integer> getRadioIdObservable() {
        return radioIdObserver;
    }

    private void initPlayer(String source) {
        try {
            currentSource = source;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
            statusObserver.onNext(Status.isPlay);
            radioIdObserver.onNext(mediaPlayer.getAudioSessionId());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            closeMediaPlayer();
            statusObserver.onNext(Status.Error);
        }
    }

    private void changeSource(String source) {
        try {
            currentSource = source;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepareAsync();
            statusObserver.onNext(Status.isPlay);
        } catch (IOException e) {
            e.printStackTrace();
            closeMediaPlayer();
            statusObserver.onNext(Status.Error);
        }
    }

    void setChangePlaySubject(Subject<String, String> changeStateSubject) {
        changeStateSubject
                .subscribe(new Subscriber<String>() {
                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   statusObserver.onNext(Status.Error);
                               }

                               @Override
                               public void onNext(String source) {
                                   if (mediaPlayer == null) {
                                       initPlayer(source);
                                   } else {
                                       if (!source.equals(currentSource)) {
                                           changeSource(source);
                                       } else {
                                           if (mediaPlayer.isPlaying()) {
                                               mediaPlayer.pause();
                                               statusObserver.onNext(Status.isStop);
                                           } else {
                                               mediaPlayer.start();
                                               statusObserver.onNext(Status.isPlay);
                                           }
                                       }
                                   }
                               }
                           }
                );
    }

    void closeMediaPlayer() {
        currentSource = "";
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            statusObserver.onNext(Status.isStop);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        statusObserver.onNext(Status.Error);
        return true;
    }
}
