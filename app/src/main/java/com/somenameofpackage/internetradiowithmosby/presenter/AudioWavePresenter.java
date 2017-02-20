package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.visualizer.RadioVisualizer;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.WaveView;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

public class AudioWavePresenter extends MvpBasePresenter<WaveView> {
    @Inject
    RadioVisualizer radioVisualizer;
    private ServiceConnection serviceConnection;
    private boolean isBind = false;
    private boolean canSow = false;

    public AudioWavePresenter(Context context) {
        RadioApplication.getComponent().injectsAudioWavePresenter(this);
        serviceConnection = new AudioWaveServiceConnection();
        context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Subscriber<Integer> getRadioModelObserver() {
        return new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                radioVisualizer.stop();
            }

            @Override
            public void onNext(Integer integer) {
                radioVisualizer.setupVisualizerFxAndUI(integer);
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
        visualiserInit();
    }

    private void visualiserInit() {
        if (canSow) {
            radioVisualizer.getVisualizerObservable().subscribe(getStationsObserver());
        }
    }

    private class AudioWaveServiceConnection implements ServiceConnection {
        Subscriber<Integer> playerIdSubscriber = getRadioModelObserver();

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            ((RadioService.RadioBinder) iBinder).subscribePlayerId(playerIdSubscriber);
            visualiserInit();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (playerIdSubscriber.isUnsubscribed()) playerIdSubscriber.unsubscribe();
            radioVisualizer.stop();
            isBind = false;
        }
    }
}
