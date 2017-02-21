package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.DataBase;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;

public class ControlPresenter extends MvpBasePresenter<RadioView> {
    @Inject
    DataBase dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;
    private BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
    private String currentStation = "";

    public ControlPresenter(Context context) {
        serviceConnection = new RadioServiceConnection();
        RadioApplication.getComponent().injectsControlPresenter(this);
        dataBase.setDefaultValues(context);
        bindToRadioService(context);
    }

    private void bindToRadioService(Context context) {
        if (!isBind)
            context.bindService(new Intent(context, RadioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void changePlayState() {
        dataBase.getCurrentStation()
                .subscribe(station -> {
                    currentStation = station.getSource();
                    changePlayStateSubject.onNext(currentStation);
                });
    }

    public void onPause(Context context) {
        if (isBind) context.unbindService(serviceConnection);
    }

    private class RadioServiceConnection implements ServiceConnection {
        Subscriber<Status> statusSubscriber = getStatusSubscriber();

        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            ((RadioService.RadioBinder) binder).setChangeStateObservable(changePlayStateSubject);
            ((RadioService.RadioBinder) binder).subscribeStatus(statusSubscriber);
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null) getView().showStatus(Status.isStop);
            isBind = false;
            if (!statusSubscriber.isUnsubscribed()) statusSubscriber.unsubscribe();
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
                if (getView() != null) getView().showStatus(status);
            }
        };
    }
}
