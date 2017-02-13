package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;


import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.functions.Action1;

public class StationsListPresenter extends MvpBasePresenter<StationsView> implements RealmChangeListener {
    private final RadioStations dataBase;
    private RadioModel radioModel;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;

    public StationsListPresenter(Context context) {
        serviceConnection = new StationsListServiceConnection();
        dataBase = new RadioStations(context);
        dataBase.getPlayingStationSource().subscribe(getPlayingStationSourceObserver(context));
    }

    private Action1<String> getPlayingStationSourceObserver(Context context) {
        return currentStation -> {
            if (!isBind) {
                ((StationsListServiceConnection) serviceConnection).setSource(currentStation);
                context.bindService(new Intent(context, RadioService.class),
                        serviceConnection,
                        Context.BIND_AUTO_CREATE);
            }
            if (getView() != null) getView().showCurrentStation(currentStation);
        };
    }

    public void getStations() {
        dataBase.getStationsObservable().subscribe(getStationsObserver());
    }

    private Action1<RealmResults<Station>> getStationsObserver() {
        return stations -> {
            if (getView() != null) {
                getView().setListStation(stations);
            }
        };
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    public void startPlay(String source) {
        dataBase.setPlayStation(source);
        if (radioModel != null) radioModel.changePlayState(source);
    }

    public void addStation(String name, String source) {
        dataBase.addStation(name, source);
    }

    public void deleteStation(String source) {
        dataBase.removeStation(source);
    }

    @Override
    public void onChange() {
        if (getView() != null) {
            getView().onChange();
        }
    }

    public void unbindService(Context context) {

        if (isBind) context.unbindService(serviceConnection);
        isBind = false;
    }

    private class StationsListServiceConnection implements ServiceConnection {
        String source;

        public void setSource(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(source);
            radioModel.changePlayState(source);
            radioModel.getRadioModelObservable().subscribe(getRadioModelObserver());
        }

        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    }

    private Action1<Status> getRadioModelObserver() {
        return status -> {
            if (getView() != null) {
                if (status != Status.isPlay) {
                    getView().disableAllStation();
                    if (status == Status.Error) {
                        getView().showCurrentStation(null);
                    }
                } else dataBase.getPlayingStationSource()
                        .subscribe(s -> getView().showCurrentStation(s));
            }
        };
    }
}
