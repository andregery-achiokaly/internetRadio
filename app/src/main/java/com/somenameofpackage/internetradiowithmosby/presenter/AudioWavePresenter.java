package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import rx.functions.Action1;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private RadioModel radioModel;
    private VisualizerModel visualizerModel;

    public AudioWavePresenter(Context context) {
        StationsRelamDB stationsRelamDB = new StationsRelamDB(context);
        final String source = stationsRelamDB.getPlayingStationSource();

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                radioModel = ((RadioService.RadioBinder) iBinder).getModel(source);
                radioModel.getRadioModelObservable().subscribe(getRadioModelObserver());
                visualizerModel = new VisualizerModel(radioModel);
                visualizerModel.getVisualizerObservable()
                        .subscribe(getStationsObserver());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Action1<Status> getRadioModelObserver() {
        return status -> {
            if (status == Status.isPlay) visualizerModel.setupVisualizerFxAndUI();
        };
    }

    private Action1<Byte[]> getStationsObserver() {
        return bytes -> {
            if (getView() != null) getView().updateVisualizer(bytes);
        };
    }
}
