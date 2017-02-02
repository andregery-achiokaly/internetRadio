package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.VisualizerListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private RadioModel radioModel;
    private VisualizerModel visualizerModel;
    private VisualizerListener visualizerListener = new VisualizerListener() {
        @Override
        public void updateVisualizer(byte[] bytes) {
            if (getView() != null) getView().updateVisualizer(bytes);
        }
    };


    private RadioListener radioListener = new RadioListener() {
        @Override
        public void onPlay() {
            visualizerModel.setupVisualizerFxAndUI();
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onError(String message) {
            Log.v(getClass().getSimpleName(), message);
        }
    };

    public AudioWavePresenter(Context context) {
        StationsRelamDB stationsRelamDB = new StationsRelamDB(context);
        final String source = stationsRelamDB.getPlayingStationSource();

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                radioModel = ((RadioService.RadioBinder) iBinder).getModel(radioListener, source);
                visualizerModel = new VisualizerModel(radioModel, visualizerListener);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        PlayerUtil.initPlayerService(serviceConnection, context);
    }
}
