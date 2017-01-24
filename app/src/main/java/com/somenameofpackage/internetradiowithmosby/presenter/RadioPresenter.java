package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.view.controlUI.RadioView;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private ServiceConnection serviceConnection;
    private RadioListener radioListener;
    private boolean bound = false;
    private StationsDB stationsDB;

    public RadioPresenter(Context context) {
        stationsDB = new StationsDB(context);
        radioListenerInit();
        String source = stationsDB.getPlaying().getSource();
        serviceConnection = new RadioServiceConnection(source);
        PlayerUtil.initPlayerService(serviceConnection, context);
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay(String message) {
                if (getView() != null) getView().showStatus(message);
            }

            @Override
            public void onPause(String message) {
                if (getView() != null) getView().showStatus(message);
            }

            @Override
            public void onError(String message) {
                if (getView() != null) getView().showStatus(message);
            }
        };
    }

    public void startPlaying(Context context) {
        if (getView() != null) getView().showStatus("Wait...");
        String source = stationsDB.getPlaying().getSource();
        stationsDB.setPlayStation(source);
        if (!bound) {
            serviceConnection = new RadioServiceConnection(source);
            PlayerUtil.initPlayerService(serviceConnection, context);
        }
        if (radioModel != null) {
            radioModel.startPlay(source);
        }
    }

    public void stopPlaying(Context context) {
        if (radioModel != null) {
            radioModel.stopPlay();
        }
        PlayerUtil.stopService(context);
    }

    private class RadioServiceConnection implements ServiceConnection {
        String source;

        RadioServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            bound = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            if (getView() != null) getView().showStatus("Wait...");
            radioModel.startPlay(source);
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            if (getView() != null)
                getView().showStatus("Service disconnected. Something went wrong!");
        }
    }
}
