package com.somenameofpackage.internetradiowithmosby.presenter;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.Station;
import com.somenameofpackage.internetradiowithmosby.model.realmDB.StationsDB;
import com.somenameofpackage.internetradiowithmosby.view.radioList.StationsView;

import io.realm.RealmResults;

import static com.somenameofpackage.internetradiowithmosby.presenter.PlayerUtil.initPlayerService;

public class StationsPresenter extends MvpBasePresenter<StationsView> {
    private StationsDB stationsDB;
    private boolean bound = false;
    private RadioModel radioModel;
    private RadioListener radioListener;

    public StationsPresenter(Context context) {
        stationsDB = new StationsDB(context);
        radioListenerInit();
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay(String message) {
                getView().showCurrentStation();
            }

            @Override
            public void onPause(String message) {

            }

            @Override
            public void onError(String message) {

            }
        };
    }

    public RealmResults<Station> getStations() {
        return stationsDB.getStations();
    }

    public void closeBD() {
        stationsDB.closeBD();
    }

    private ServiceConnection getServiceConnectionInit(final String source) {
        return new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
                radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
                radioModel.startPlay(source);
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
    }


    public void startPlay(String source, Context context) {
        stationsDB.setPlayStation(source);
        if (!bound) {
            initPlayerService(getServiceConnectionInit(source), context);
        }
        if (radioModel != null) {
            radioModel.startPlay(source);
            getView().showCurrentStation();
        }
    }

    public void startChangeStation() {

    }
}
