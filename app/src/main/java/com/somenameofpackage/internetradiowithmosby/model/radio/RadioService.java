package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.somenameofpackage.internetradiowithmosby.presenter.listeners.RadioListener;
import com.somenameofpackage.internetradiowithmosby.ui.notifications.RadioNotification;

public class RadioService extends Service {
    final static public String ACTION = "ACTION";
    final static public String PLAY = "PLAY";
    private final static String CLOSE = "CLOSE";
    Notification notification;

    private final RadioModel radioModel = new RadioModel();

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new RadioNotification(getBaseContext()).getNotification();
        startForeground(223, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int indicate = super.onStartCommand(intent, flags, startId);
        String action = intent.getStringExtra("ACTION");
        switch (action) {
            case PLAY:
                radioModel.changePlayState();
                break;
            case CLOSE:
                radioModel.changePlayState();
                stopSelf();
                break;
        }
        return indicate;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(223, new RadioNotification(getBaseContext(), "124").getNotification());
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
    }
}
