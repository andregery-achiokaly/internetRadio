package com.somenameofpackage.internetradiowithmosby;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import static android.support.v4.app.ServiceCompat.START_STICKY;

class RadioPresenter extends MvpBasePresenter<RadioView> {
    private RadioModel radioModel;
    private RadioListener radioListener = new RadioListener() {
        @Override
        public void onPlay(String message) {
            getView().showMessage(message);
        }

        @Override
        public void onPause(String message) {
            getView().showMessage(message);
        }

        @Override
        public void onError(String message) {
            getView().showMessage(message);
        }
    };

    private boolean bound = false;

    private void initService(final String source, Context context) {
        Intent intent = new Intent(context, RadioService.class);
        intent.addFlags(START_STICKY);
        context.startService(intent);

        ServiceConnection sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                bound = true;
                radioModel = ((RadioService.RadioBinder) binder).getModel(radioListener, source);
                if (radioModel != null) {
                    radioModel.startPlay(source);
                }
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
                getView().showMessage("Disconnect from Service. Something went wrong!");
            }
        };

        context.bindService(intent, sConn, Context.BIND_AUTO_CREATE);
    }

    void startPlaying(String source, Context context) {
        if (!bound) {
            initService(source, context);
        }
        if (radioModel != null) radioModel.startPlay(source);
    }

    void stopPlaying() {
        if (radioModel != null) radioModel.stopPlay();
    }

    public void detachView(boolean retainPresenterInstance) {
        super.detachView(retainPresenterInstance);
        if (!retainPresenterInstance) {
//            if (radioModel != null) radioModel.closeMediaPlayer();
        }
    }
}
