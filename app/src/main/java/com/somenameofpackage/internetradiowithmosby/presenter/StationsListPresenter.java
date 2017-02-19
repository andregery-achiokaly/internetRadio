package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class StationsListPresenter extends MvpBasePresenter<StationsView> {
    @Inject
    DataBase dataBase;
    private boolean isBind = false;
    private ServiceConnection servCon;
    private PublishSubject<String> changePlayStateSabject;

    public StationsListPresenter(Context context) {
        RadioApplication.getComponent().injectsStationsListPresenter(this);
        servCon = new StationsListServiceConnection();
        if (!isBind) context.bindService(new Intent(context, RadioService.class), servCon, Context.BIND_AUTO_CREATE);

        dataBase.setDefaultValues(context);
        dataBase.getPlayingStationSource()
                .filter(s -> getView() != null && s.isLoaded() && s.isValid())
                .subscribe(s -> getView().showCurrentStation(s.getSource()));
    }

    public void getStations() {
        dataBase.getStations()
                .filter(stations -> stations.isLoaded() && stations.isValid())
                .filter(stations -> getView() != null)
                .subscribe(stations -> getView().setListStations(stations));
    }

    public void closeBD() {
        dataBase.closeBD();
    }

    public void addStation(String name, String source) {
        dataBase.addStation(new Station(name, source));
    }

    public void deleteStation(String source) {
        dataBase.deleteStation(source);
    }

    public void onPause(Context context) {
        if (isBind) context.unbindService(servCon);
    }

    public void setChangePlayStateSubject(PublishSubject<String> changePlayStateSabject) {
        this.changePlayStateSabject = changePlayStateSabject;
    }

    private class StationsListServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).subscribeStatus(getRadioModelObserver());
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSabject);
        }

        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    }

    private Action1<Status> getRadioModelObserver() {
        return status -> {
            if (getView() != null) {
                if (status == Status.Error) getView().showCurrentStation(null);
                if (status == Status.isPlay) {
                    dataBase.getPlayingStationSource()
                            .filter(station -> station.isLoaded() && station.isValid())
                            .subscribe(station -> getView().showCurrentStation(station.getSource()));
                }
            }
        };
    }
}
