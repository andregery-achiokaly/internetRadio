package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import javax.inject.Inject;

import rx.functions.Action1;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    @Inject
    VisualizerModel visualizerModel;
    private ServiceConnection serviceConnection;
    private boolean isBind = false;
    private boolean canSow = false;

    public AudioWavePresenter(Context context) {
        RadioApplication.getComponent().injectsAudioWavePresenter(this);
        serviceConnection = new AudioWaveServiceConnection();
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Action1<Integer> getRadioModelObserver() {
        return status -> visualizerModel.setupVisualizerFxAndUI(status);
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
        vizualizerInit();
    }

    private void vizualizerInit() {
        if (canSow) {
            visualizerModel.getVisualizerObservable()
                    .subscribe(getStationsObserver());
        }
    }

    private class AudioWaveServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            ((RadioService.RadioBinder) iBinder).subscribePlayerId(getRadioModelObserver());
            vizualizerInit();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    }
}
