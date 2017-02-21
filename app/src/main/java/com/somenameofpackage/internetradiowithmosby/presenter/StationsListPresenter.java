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
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;

public class StationsListPresenter extends MvpBasePresenter<StationsView> {
    @Inject
    DataBase dataBase;
    private boolean isBind = false;
    private ServiceConnection servCon;
    private BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();

    public StationsListPresenter(Context context) {
        RadioApplication.getComponent().injectsStationsListPresenter(this);
        servCon = new StationsListServiceConnection();
        if (!isBind) context.bindService(new Intent(context, RadioService.class), servCon, Context.BIND_AUTO_CREATE);

        dataBase.setDefaultValues(context);
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

    public void deleteStation(String source) {
        dataBase.deleteStation(source);
    }

    public void onPause(Context context) {
        if (isBind) context.unbindService(servCon);
        isBind = false;
    }

    public void stationClick(Station station) {
        dataBase.setPlayingStationSource(station);
        changePlayStateSubject.onNext(station.getSource());
    }

    private class StationsListServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
        }

        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    }
}
