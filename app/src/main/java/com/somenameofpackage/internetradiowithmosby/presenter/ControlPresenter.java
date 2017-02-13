package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.db.RadioStations;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.views.RadioView;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;

import rx.functions.Action1;

public class ControlPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private final RadioStations dataBase;
    private boolean isBind = false;
    private ServiceConnection serviceConnection;

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
                context.startService(new Intent(context, RadioService.class));
                ((RadioServiceConnection) serviceConnection).setSource(value);
                context.bindService(new Intent(context, RadioService.class),
                        serviceConnection,
                        Context.BIND_AUTO_CREATE);
                isBind = true;
            }
        };
    }

    private Action1<String> getChangePlayStateObserver() {
        return value -> {
            if (radioModel != null) {
                if (getView() != null) getView().showStatus(Status.Wait);
                radioModel.changePlayState(value);
            }
        };
    }

    public void unbindService(Context context) {
        if (isBind) context.unbindService(serviceConnection);
        isBind = false;
    }

    private class RadioServiceConnection implements ServiceConnection {
        String source;

        public void setSource(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(source);
            radioModel.changePlayState(source);
            radioModel.getRadioModelObservable()
                    .subscribe(status -> {
                        if (getView() != null) {
                            getView().showStatus(status);
                        }
                    }, e -> {
                        if (getView() != null) getView().showStatus(Status.Error);
                    }, () -> {
                    });
        }

        public void onServiceDisconnected(ComponentName name) {
            if (getView() != null)
                getView().showStatus(Status.isStop);
        }
    }
}
