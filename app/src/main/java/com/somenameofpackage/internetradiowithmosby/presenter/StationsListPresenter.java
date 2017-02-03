package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import io.realm.RealmResults;

public class StationsListPresenter extends MvpBasePresenter<StationsView> {
    private final RadioStations dataBase;
    private RadioModel radioModel;
    private RadioListener radioListener;

    public StationsListPresenter(Context context) {
        dataBase = new RadioStations(context);
        radioListenerInit();
        String source = dataBase.getPlayingStationSource();
        context.bindService(new Intent(context, RadioService.class),
                new StationsListServiceConnection(source),
                Context.BIND_AUTO_CREATE);
    }

    public RealmResults<Station> getStations() {
        return dataBase.getStations();
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    public void startPlay(String source, Context context) {
        dataBase.setPlayStation(source);
        if (radioModel != null) {
            radioModel.play(source);
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
        final String source;

        StationsListServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            radioModel.play(source);
        }

        public void onServiceDisconnected(ComponentName name) {
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
