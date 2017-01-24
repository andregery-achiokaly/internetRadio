package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioModel;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.view.controlUI.RadioView;

import static com.somenameofpackage.internetradiowithmosby.presenter.PlayerUtil.initPlayerService;

public class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private ServiceConnection serviceConnection;
    private RadioListener radioListener;

    private boolean bound = false;

    public RadioPresenter() {
        radioListenerInit();
    }

    private void radioListenerInit() {
        radioListener = new RadioListener() {
            @Override
            public void onPlay(String message) {
                getView().showStatus(message);
            }

            @Override
            public void onPause(String message) {
                getView().showStatus(message);
            }

            @Override
            public void onError(String message) {
                getView().showStatus(message);
            }
        };
    }

    public void startPlaying(String source, Context context) {
        if (!bound) {
            serviceConnection = new RadioPresenterServiceConnection(source);
            initPlayerService(serviceConnection, context);
        }
        if (radioModel != null) radioModel.startPlay(source);
    }

    public void stopPlaying(Context context) {
        if (radioModel != null) {
            radioModel.stopPlay();
        }
        PlayerUtil.stopService(context);
    }

    void closePlayer(Context context) {
        radioModel.closeMediaPlayer();
        context.unbindService(serviceConnection);
        PlayerUtil.stopService(context);
    }

    class RadioPresenterServiceConnection implements ServiceConnection {
        String source;

        public RadioPresenterServiceConnection(String source) {
            this.source = source;
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            bound = true;
            radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
            getView().showStatus("Wait...");
            radioModel.startPlay(source);
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            getView().showStatus("Service disconnected. Something went wrong!");
        }
    }
}
