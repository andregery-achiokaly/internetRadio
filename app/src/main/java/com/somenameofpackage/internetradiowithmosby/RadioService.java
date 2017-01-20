package com.somenameofpackage.internetradiowithmosby;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class RadioService extends Service {
    RadioModel radioModel = new RadioModel();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

    class RadioBinder extends Binder {
        RadioModel getModel(RadioListener listener, String source) {
            radioModel.setSource(source);
            radioModel.setRadioListener(listener);
            return radioModel;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioModel.closeMediaPlayer();
    }
}
