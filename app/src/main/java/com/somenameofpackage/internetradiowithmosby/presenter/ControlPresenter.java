package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class ControlPresenter extends MvpBasePresenter<RadioView> {
    @Inject
    RadioStations dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;
    private PublishSubject<String> changePlayStateSubject = PublishSubject.create();

    public ControlPresenter(Context context) {
        serviceConnection = new RadioServiceConnection();
        RadioApplication.getComponent().injectsControlPresenter(this);
        dataBase.initDB(context);
        dataBase.getPlayingStationSource().subscribe(getBindServiceObserver(context));
    }

    public void changePlayState() {
        dataBase.getPlayingStationSource().subscribe(getChangePlayStateObserver());
    }

    private Action1<String> getBindServiceObserver(Context context) {
        return value -> {
            if (!isBind) {
                context.bindService(new Intent(context, RadioService.class),
                        serviceConnection,
                        Context.BIND_AUTO_CREATE);
            }
        };
    }

    private Action1<String> getChangePlayStateObserver() {
        return newStationSource -> changePlayStateSubject.onNext(newStationSource);
    }

    public void onPause(Context context) {
        if (isBind) context.unbindService(serviceConnection);
    }

    private class RadioServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
            ((RadioService.RadioBinder) binder).subscribeStatus(getStatusSubscriber());
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null) getView().showStatus(Status.isStop);
            isBind = false;
        }
    }

    @NonNull
    private Action1<Status> getStatusSubscriber() {
        return status -> {
            if (getView() != null) getView().showStatus(status);
        };
    }
}
