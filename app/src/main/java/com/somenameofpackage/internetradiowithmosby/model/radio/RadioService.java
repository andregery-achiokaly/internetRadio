package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.notifications.RadioNotification;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class RadioService extends Service {
    final static public String ACTION = "ACTION";
    final static public String PLAY = "PLAY";
    private Notification notification;
    @Inject
    RadioModel radioModel;

    @Override
    public void onCreate() {
        super.onCreate();
        RadioApplication.getComponent().injectsRadioService(this);

        notification = new RadioNotification(getBaseContext()).getNotification();
        radioModel.getRadioModelStatusObservable().subscribe(getRadioModelObserver());
        startForeground(RadioNotification.ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
            if (intent != null) {
                String action = intent.getStringExtra(ACTION);
                if (action!= null && action.equals(PLAY)) radioModel.changePlayState(null);
            }
        return Service.START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

    public class RadioBinder extends Binder {
        public RadioModel getModel() {
            return radioModel;
        }

        public void subscribeStatus(Subscriber<Status> subscriber){
            radioModel.getRadioModelStatusObservable().subscribe(subscriber);
        }

        public void subscribeStatus(Action1<Status> subscriber){
            radioModel.getRadioModelStatusObservable().subscribe(subscriber);
        }

        public void subscribePlayerId(Action1<Integer> subscriber){
            radioModel.getRadioIdObservable().subscribe(subscriber);
        }

        public void setChangeStateObservable(PublishSubject<String> changeStateSubject) {
            radioModel.setChangePlaySubject(changeStateSubject);
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
