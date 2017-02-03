package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private RadioListener radioListener;
    private boolean isPlay = false;
    private final RadioStations dataBase;

    public RadioPresenter(Context context) {
        dataBase = new RadioStations(context);
        radioListenerInit();
        String source = dataBase.getPlayingStationSource();
        context.bindService(new Intent(context, RadioService.class),
                    new RadioServiceConnection(source),
                    Context.BIND_AUTO_CREATE);
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

    public void buttonPressed() {
        if (isPlay) {
            isPlay = false;
            startPlaying();
        } else {
            isPlay = true;
            stopPlaying();
        }
    }

    private void startPlaying() {
        String source = dataBase.getPlayingStationSource();
        dataBase.setPlayStation(source);
        if (radioModel != null) {
            radioModel.play(source);
        }
    }

    private void stopPlaying() {
        String source = dataBase.getPlayingStationSource();
        dataBase.setPlayStation(source);
        if (radioModel != null) {
            radioModel.play(source);
        }
    }

    private class RadioServiceConnection implements ServiceConnection {
        final String source;

        RadioServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            radioModel.play(source);
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null)
                getView().showStatus(Status.Stop);
        }
    }
}
