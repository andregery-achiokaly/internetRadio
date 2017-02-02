package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStation;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.DBChangeListener;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import java.util.List;

public class StationsListPresenter extends MvpBasePresenter<StationsView> {
    private RadioStations dataBase;
    private boolean bound = false;
    private RadioModel radioModel;
    private RadioListener radioListener;
    private DBChangeListener dbChangeListener = new DBChangeListener() {
        @Override
        public void update() {
            if (getView() != null) getView().updateStations();
        }
    };

    public StationsListPresenter(Context context) {
        dataBase = new RadioStations(context);
        dataBase.addListener(dbChangeListener);
        radioListenerInit();
        String source = dataBase.getPlayingStationSource();
        PlayerUtil.initPlayerService(new StationsListServiceConnection(source), context);
    }

    public List<RadioStation> getStations() {
        return dataBase.getStations();
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    public void startPlay(String source, Context context) {
        dataBase.setPlayStation(source);
        if (!bound) {
            PlayerUtil.initPlayerService(new StationsListServiceConnection(source), context);
        }
        if (radioModel != null) {
            radioModel.startPlay(source);
            if (getView() != null)
                getView().showCurrentStation(dataBase.getPlayingStationSource());
        }
    }

    public void addStation(String name, String source, Bitmap icon) {
        dataBase.addStation(name, source, icon);
    }

    public void deleteStation(String source) {
        dataBase.removeStation(source);
    }

    private class StationsListServiceConnection implements ServiceConnection {
        String source;

        StationsListServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            bound = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            radioModel.startPlay(source);
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay() {
                if (getView() != null) {
                    getView().showCurrentStation(dataBase.getPlayingStationSource());
                }
            }

            @Override
            public void onPause() {
                if (getView() != null)
                    getView().disableAllStation();
            }

            @Override
            public void onError(String message) {
                Log.v(getClass().getSimpleName(), message);
            }
        };
    }
}
