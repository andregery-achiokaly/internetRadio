package com.somenameofpackage.internetradiowithmosby;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;

    RadioModel(){
        Log.v("GGG", "Radiomodel create");
    }

    public void stopPlay(){
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void startPlay(String source){
        closeMediaPlayer();
        createMediaPlayer(source);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
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

    public void closePlayer(){
        closeMediaPlayer();
    }
}
