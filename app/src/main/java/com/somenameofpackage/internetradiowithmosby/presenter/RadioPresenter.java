package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private final RadioStations dataBase;
    private boolean isBind = false;

    public RadioPresenter(Context context) {
        dataBase = new RadioStations(context);
        dataBase.getPlayingStationSource().subscribe(getBindServiceObserver(context));
    }

    private Observer<String> getRadioModelObserver() {
        return new DefaultObserver<String>() {
            @Override
            public void onNext(String value) {
                if (getView() != null) {
                    if (!value.equals("")) getView().showStatus(Status.Play);
                    else getView().showStatus(Status.Stop);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
                if (getView() != null) getView().showStatus(Status.Error);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void changePlayState() {
        dataBase.getPlayingStationSource().subscribe(getChangePlayStateObserver());
    }

    private Observer<String> getBindServiceObserver(Context context) {
        return new DefaultObserver<String>() {
            @Override
            public void onNext(String value) {
                if (!isBind) {
                    context.bindService(new Intent(context, RadioService.class),
                            new RadioServiceConnection(value),
                            Context.BIND_AUTO_CREATE);
                    isBind = true;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observer<String> getChangePlayStateObserver() {
        return new DefaultObserver<String>() {
            @Override
            public void onNext(String value) {
                if (radioModel != null) {
                    radioModel.changePlayState(value);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private class RadioServiceConnection implements ServiceConnection {
        final String source;

        RadioServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            radioModel = ((RadioService.RadioBinder) binder).getModel(source);
            radioModel.changePlayState(source);
            radioModel.getRadioModelObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getRadioModelObserver());
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null)
                getView().showStatus(Status.Stop);
        }
    }
}
