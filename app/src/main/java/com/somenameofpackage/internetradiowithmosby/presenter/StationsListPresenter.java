package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.RadioStation;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import io.realm.RealmResults;

public class StationsListPresenter extends MvpBasePresenter<StationsView> {
    private StationsDB stationsDB;
    private boolean bound = false;
    private RadioModel radioModel;
    private RadioListener radioListener;

    public StationsListPresenter(Context context) {
        stationsDB = new StationsDB(context);
        radioListenerInit();
        String source = stationsDB.getPlaying().getSource();
        PlayerUtil.initPlayerService(new StationsListServiceConnection(source), context);
    }

    public RealmResults<RadioStation> getStations() {
        return stationsDB.getStations();
    }

    public void closeBD() {
        stationsDB.closeBD();
    }

    public void startPlay(String source, Context context) {
        stationsDB.setPlayStation(source);
        if (!bound) {
            PlayerUtil.initPlayerService(new StationsListServiceConnection(source), context);
        }
        if (radioModel != null) {
            radioModel.startPlay(source);
            if (getView() != null)
                getView().showCurrentStation(stationsDB.getNumberOfPlayingStation());
        }
    }

    public void addStation(String name, String source, Bitmap icon) {
        stationsDB.addStation(name, source, icon);
    }

    public void deleteStation(String source) {
        stationsDB.deleteStation(source);
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
                    getView().showCurrentStation(stationsDB.getNumberOfPlayingStation());
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
