package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.view.audioWave.WaveView;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private MediaPlayer mediaPlayer;
    private RadioListener radioListener = new RadioListener() {
        @Override
        public void onPlay(String message) {

        }

        @Override
        public void onPause(String message) {

        }

        @Override
        public void onError(String message) {

        }
    };

    public AudioWavePresenter(Context context) {
        StationsDB stationsDB = new StationsDB(context);
        String source = stationsDB.getPlaying().getSource();

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                RadioModel radioModel = ((RadioService.RadioBinder) iBinder).getModel(radioListener, source);
                mediaPlayer = radioModel.getMediaPlayer();
                setupVisualizerFxAndUI();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        PlayerUtil.initPlayerService(serviceConnection, context);
    }

    private void setupVisualizerFxAndUI() {
        Visualizer mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Mv(), Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    private class Mv implements Visualizer.OnDataCaptureListener {
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            if (getView() != null) getView().updateVisualizer(bytes);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

        }
    }
}
