package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.RadioService;
import com.somenameofpackage.internetradiowithmosby.view.RadioView;

import static android.support.v4.app.ServiceCompat.START_STICKY;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private ServiceConnection serviceConnection;
    private RadioListener radioListener;

    private boolean bound = false;

    public RadioPresenter() {
        radioListenerInit();
    }

    private void radioListenerInit(){
        radioListener = new RadioListener() {
            @Override
            public void onPlay(String message) {
                getView().showMessage(message);
            }

            @Override
            public void onPause(String message) {
                getView().showMessage(message);
            }

            @Override
            public void onError(String message) {
                getView().showMessage(message);
            }
        };
    }

    private void initService(final String source, Context context) {
        startService(context);
        bindToService(source, context);
    }

    private void startService(Context context) {
        Intent intent = new Intent(context, RadioService.class);
        intent.addFlags(START_STICKY);
        context.startService(intent);
    }

    private void bindToService(final String source, Context context) {
        Intent intent = new Intent(context, RadioService.class);
        serviceConnectionInit(source);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void serviceConnectionInit(final String source) {
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
                radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
                getView().showMessage("Wait...");
                radioModel.startPlay();
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
                getView().showMessage("Service disconnected. Something went wrong!");
            }
        };
    }

    public void startPlaying(String source, Context context) {
        if (!bound) {
            initService(source, context);
        }
        if (radioModel != null) radioModel.startPlay();
    }

    public void stopPlaying() {
        if (radioModel != null) radioModel.stopPlay();
    }

    void closePlayer(Context context){
        radioModel.closeMediaPlayer();
        context.unbindService(serviceConnection);
    }
}
