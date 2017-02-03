package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.notifications.RadioNotification;

public class RadioService extends Service {
    final static public String ACTION = "ACTION";
    final static public String PLAY = "PLAY";
    private final static String CLOSE = "CLOSE";

    private final RadioModel radioModel = new RadioModel();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("GGG", "onCreate");

        startForeground(23, new RadioNotification(getBaseContext()).getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int indicate = super.onStartCommand(intent, flags, startId);
        String action = intent.getStringExtra("ACTION");
        switch (action){
            case PLAY:
                radioModel.play();
                Log.v("GGG", "Play");
                break;
            case CLOSE:
                Log.v("GGG", "CLOSE");
                radioModel.play();
                stopSelf();
                break;
        }
        return indicate;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

    public class RadioBinder extends Binder {
        public RadioModel getModel(RadioListener listener, String source) {
            radioModel.setSource(source);
            radioModel.addRadioListener(listener);
            return radioModel;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioModel.closeMediaPlayer();
        Log.v("GGG", "DESTROY");
    }
}
