package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import rx.Subscriber;
import rx.functions.Action1;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private VisualizerModel visualizerModel;
    private ServiceConnection serviceConnection;
    private boolean isBind = false;
    private boolean canSow = false;

    public AudioWavePresenter(Context context) {
        StationsRelamDB stationsRelamDB = new StationsRelamDB(context);
        final String source = stationsRelamDB.getPlayingStationSource();

        serviceConnectionInit(source);
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void serviceConnectionInit(final String source) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                isBind = true;
                ((RadioService.RadioBinder) iBinder).subscribePlayerId(getRadioModelObserver());
                visualizerModel = new VisualizerModel();
                visualizerModel.getVisualizerObservable()
                        .subscribe(getStationsObserver());
                radioModel = ((RadioService.RadioBinder) iBinder).getModel(source);
                radioModel.getRadioModelObservable().subscribe(getRadioModelObserver());
                if (visualizerModel == null && canSow) {
                    vizualizerInit();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBind = false;
            }
        };
    }

    private Action1<Integer> getRadioModelObserver() {
        return id -> {
            visualizerModel.setupVisualizerFxAndUI(id);
    private Action1<Status> getRadioModelObserver() {
        return status -> {
            if (status == Status.isPlay && canSow && visualizerModel != null) {
                visualizerModel.setupVisualizerFxAndUI();
            }
        };
    }

    private Action1<Byte[]> getStationsObserver() {
        return bytes -> {
            if (getView() != null && canSow) {
                getView().updateVisualizer(bytes);
            }
        };
    }

    public void onPause(Context context) {
        if (isBind) context.unbindService(serviceConnection);
        isBind = false;
    }

    public void setCanShow(boolean canSow) {
        this.canSow = canSow;
        if (canSow && radioModel != null) {
            vizualizerInit();
        }
    }

    private void vizualizerInit() {
        visualizerModel = new VisualizerModel(radioModel);
        visualizerModel.getVisualizerObservable().subscribe(getStationsObserver());
        visualizerModel.setupVisualizerFxAndUI();
    }
}
