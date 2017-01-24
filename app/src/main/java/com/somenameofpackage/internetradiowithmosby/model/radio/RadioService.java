package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.somenameofpackage.internetradiowithmosby.presenter.RadioListener;

public class RadioService extends Service {
    RadioModel radioModel = new RadioModel();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

   public class RadioBinder extends Binder {
        public RadioModel getModel(RadioListener listener, String source) {
            radioModel.setSource(source);
            radioModel.setRadioListener(listener);
            return radioModel;
        }
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioModel.closeMediaPlayer();
    }
}
