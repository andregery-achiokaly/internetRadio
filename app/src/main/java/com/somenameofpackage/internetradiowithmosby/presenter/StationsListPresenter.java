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
import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.views.StationsView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.observers.DefaultObserver;
import io.realm.RealmChangeListener;

public class StationsListPresenter extends MvpBasePresenter<StationsView> implements RealmChangeListener {
    private final RadioStations dataBase;
    private RadioModel radioModel;
    private RadioListener radioListener;
    private boolean isBind = false;

    public StationsListPresenter(Context context) {
        dataBase = new RadioStations(context);
        radioListenerInit();
        dataBase.getPlayingStationSource().subscribe(getPlayingStationSourceObserver(context));
    }

    private Observer<String> getPlayingStationSourceObserver(Context context) {
        return new DefaultObserver<String>() {
            @Override
            public void onNext(String value) {
                if (!isBind) {
                    context.bindService(new Intent(context, RadioService.class),
                            new StationsListServiceConnection(value),
                            Context.BIND_AUTO_CREATE);
                    isBind = true;
                }
                if (getView() != null) getView().showCurrentStation(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void getStations() {
        dataBase.getStationsObservable().subscribe(getStationsObserver());
    }

    private Observer<List<Station>> getStationsObserver() {
        return new DefaultObserver<List<Station>>() {
            @Override
            public void onNext(List<Station> value) {
                if (getView() != null) getView().setListStation(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

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
        getView().onChange();
    }

    private class StationsListServiceConnection implements ServiceConnection {
        final String source;

        StationsListServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            radioModel.changePlayState(source);
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay(String source) {
                if (getView() != null) {
                    getView().showCurrentStation(source);
                }
            }

            @Override
            public void onPause() {
                if (getView() != null)
                    getView().disableAllStation();
            }

            @Override
            public void onError(String message) {
                Log.e(getClass().getSimpleName(), message);
            }
        };
    }

    @Override
    public void detachView(boolean retainInstance) {

        super.detachView(retainInstance);
    }
}
