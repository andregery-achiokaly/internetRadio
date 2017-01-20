package com.somenameofpackage.internetradiowithmosby;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class RadioModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;
    private RadioListener radioListener;

    RadioModel(RadioListener radioListener){
        this.radioListener = radioListener;
        Log.v("GGG", "Radiomodel create");
    }

    public void stopPlay(){
       if(mediaPlayer!=null) {
           if (mediaPlayer.isPlaying()) {
               mediaPlayer.pause();
               radioListener.onPause("I'm stop");
           }
       }
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
        radioListener.onPlay("I'm playing");
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
            radioListener.onError("Oh no... Something went wrong :(");
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
