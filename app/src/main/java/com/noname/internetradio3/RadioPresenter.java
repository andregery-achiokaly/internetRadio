package com.noname.internetradio3;


import android.media.AudioManager;
import android.media.MediaPlayer;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;

class RadioPresenter extends MvpBasePresenter<RadioView> implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;

    void startPlaying(String source) {
        closeMediaPlayer();
        createMediaPlayer(source);
    }

    void stopPlaying() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void detachView(boolean retainPresenterInstance) {
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance) {
            closeMediaPlayer();
        }
    }

    private void createMediaPlayer(String source) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
