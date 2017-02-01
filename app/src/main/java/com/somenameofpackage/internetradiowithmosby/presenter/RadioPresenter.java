package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.db.realmDB.StationsRelamDB;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private ServiceConnection serviceConnection;
    private RadioListener radioListener;
    private boolean bound = false;
    private boolean isPlay = false;
    private RadioStations dataBase;

    public RadioPresenter(Context context) {
        dataBase = new RadioStations(context);
        radioListenerInit();
        String source = dataBase.getPlayingStationSource();
        serviceConnection = new RadioServiceConnection(source);
        PlayerUtil.initPlayerService(serviceConnection, context);
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay() {
                if (getView() != null) getView().showStatus(Status.Play);
            }

            @Override
            public void onPause() {
                if (getView() != null) getView().showStatus(Status.Stop);
            }

            @Override
            public void onError(String message) {
                if (getView() != null) getView().showStatus(Status.Error);
            }
        };
    }

    public void buttonPressed(Context context){
        if(isPlay){
            isPlay = false;
            startPlaying(context);
        } else {
            isPlay = true;
            stopPlaying();
        }
    }

    private void startPlaying(Context context) {
        String source = dataBase.getPlayingStationSource();
        dataBase.setPlayStation(source);
        if (!bound) {
            serviceConnection = new RadioServiceConnection(source);
            PlayerUtil.initPlayerService(serviceConnection, context);
        }
        if (radioModel != null) {
            radioModel.startPlay(source);
        }
    }

    private void stopPlaying() {
        if (radioModel != null) {
            radioModel.stopPlay();
        }
        bound = false;
    }

    private class RadioServiceConnection implements ServiceConnection {
        String source;

        RadioServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            bound = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            radioModel.startPlay(source);
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            if (getView() != null)
                getView().showStatus(Status.Stop);
        }
    }
}
