package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;


import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class StationsListPresenter extends MvpBasePresenter<StationsView> implements RealmChangeListener {
    @Inject
    RadioStations dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;
    private PublishSubject<String> changePlayStateSubject = PublishSubject.create();

    public StationsListPresenter(Context context) {
        serviceConnection = new StationsListServiceConnection();
        RadioApplication.getComponent().injectsStationsListPresenter(this);
        dataBase.initDB(context);
        dataBase.getPlayingStationSource().subscribe(getPlayingStationSourceObserver(context));
    }

    private Action1<String> getPlayingStationSourceObserver(Context context) {
        return currentStation -> {
            if (!isBind) {
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
                getView().setListStations(stations);
            }
        };
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    public void changePlayState(String source) {
        dataBase.setPlayStation(source);
        changePlayStateSubject.onNext(source);
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

    public void onPause(Context context) {
        if (isBind) context.unbindService(serviceConnection);
        isBind = false;
    }

    private class StationsListServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).subscribeStatus(getRadioModelObserver());
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
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
