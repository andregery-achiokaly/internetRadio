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
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;


import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class ControlPresenter extends MvpBasePresenter<RadioView> {
    private final RadioStations dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;
    private PublishSubject<String> changePlayStateSubject = PublishSubject.create();

    public ControlPresenter(Context context) {
        serviceConnection = new RadioServiceConnection();
        dataBase = new RadioStations(context);
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
        isBind = false;
    }

    private class RadioServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
            ((RadioService.RadioBinder) binder).subscribeStatus(getStatusSubscriber());
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null)
                getView().showStatus(Status.isStop);
        }
    }

    @NonNull
    private Subscriber<Status> getStatusSubscriber() {
        return new Subscriber<Status>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) getView().showStatus(Status.Error);
            }

            @Override
            public void onNext(Status status) {
                if (getView() != null) {
                    getView().showStatus(status);
                }
            }
        };
    }
}
