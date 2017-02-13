package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.somenameofpackage.internetradiowithmosby.model.db.Station;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.notifications.RadioNotification;

import rx.functions.Action1;

public class RadioService extends Service {
    final static public String ACTION = "ACTION";
    final static public String PLAY = "PLAY";
    private Notification notification;

    private final RadioModel radioModel = new RadioModel();

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new RadioNotification(getBaseContext()).getNotification();
        radioModel.getRadioModelObservable().subscribe(getRadioModelObserver());
        startForeground(RadioNotification.ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int indicate = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (action.equals(PLAY)) radioModel.changePlayState();
        }
        return indicate;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

    public class RadioBinder extends Binder {
        public RadioModel getModel(String source) {
            radioModel.setSource(source);
            return radioModel;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioModel.closeMediaPlayer();
    }

    private Action1<Status> getRadioModelObserver() {
        return status -> {
            notification = new RadioNotification(getBaseContext(), status.toString())
                    .getNotification();
            NotificationManager mNotificationManager = (NotificationManager) getBaseContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(RadioNotification.ID, notification);
        };
    }
}
