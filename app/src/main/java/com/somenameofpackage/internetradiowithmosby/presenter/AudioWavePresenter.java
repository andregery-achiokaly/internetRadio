package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.VisualizerModel;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import rx.functions.Action1;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    private RadioModel radioModel;
    private VisualizerModel visualizerModel;
    private ServiceConnection serviceConnection;
    private boolean isBind = false;
    private boolean canSow = false;

    public AudioWavePresenter(Context context) {
        serviceConnection = new AudioWaveServiceConnection();
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Action1<Integer> getRadioModelObserver() {
        return status -> {
            if (canSow && visualizerModel != null) {
                visualizerModel.setupVisualizerFxAndUI(status);
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

    public void unbindService(Context context) {
        if (isBind) context.unbindService(serviceConnection);
        isBind = false;
    }

    public void setCanShow(boolean canSow) {
        this.canSow = canSow;
        if (canSow) vizualizerInit();

    }

    private void vizualizerInit() {
        visualizerModel = new VisualizerModel();
        visualizerModel.getVisualizerObservable()
                .subscribe(getStationsObserver());
    }

    private class AudioWaveServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            ((RadioService.RadioBinder) iBinder).subscribePlayerId(getRadioModelObserver());
            if (visualizerModel == null && canSow) vizualizerInit();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    }
}
