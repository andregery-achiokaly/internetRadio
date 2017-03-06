package com.somenameofpackage.internetradiowithmosby.model.radio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.somenameofpackage.internetradiowithmosby.model.db.sqliteDB.SqliteDBHelper;
import com.somenameofpackage.internetradiowithmosby.ui.RadioApplication;
import com.somenameofpackage.internetradiowithmosby.ui.fragments.Status;
import com.somenameofpackage.internetradiowithmosby.ui.notifications.RadioNotification;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

public class RadioService extends Service {
    final static public String ACTION = "ACTION";
    final static public String PLAY = "PLAY";
    private Notification notification;
    private BehaviorSubject<String> changePlayStateSubject = BehaviorSubject.create();
    private Subscriber<Status> radioStatusSubscriber = getRadioModelObserver();

    @Inject
    Radio radio;
    @Inject
    SqliteDBHelper dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        RadioApplication.getComponent().injectsRadioService(this);

        notification = new RadioNotification(getBaseContext()).getNotification();

        radio.getRadioModelStatusObservable().subscribe(radioStatusSubscriber);
        radio.setChangePlaySubject(changePlayStateSubject);

        startForeground(RadioNotification.ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (action != null && action.equals(PLAY)) {
                changePlayStateSubject.onNext(dataBase.getCurrentStation().getSource());
            }
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RadioBinder();
    }

    public class RadioBinder extends Binder {
        public void subscribeStatus(Subscriber<Status> subscriber) {
            radio.getRadioModelStatusObservable().subscribe(subscriber);
        }

        public void subscribePlayerId(Subscriber<Integer> subscriber) {
            radio.getRadioIdObservable().subscribe(subscriber);
        }

        public void setChangeStateObservable(Subject<String, String> changeStateSubject) {
            radio.setChangePlaySubject(changeStateSubject);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!radioStatusSubscriber.isUnsubscribed()) radioStatusSubscriber.unsubscribe();
        radio.closeMediaPlayer();
    }

    private Subscriber<Status> getRadioModelObserver() {
        return new Subscriber<Status>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                notification = new RadioNotification(getBaseContext(), Status.Error.toString())
                        .getNotification();
                NotificationManager mNotificationManager = (NotificationManager) getBaseContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(RadioNotification.ID, notification);
            }

            @Override
            public void onNext(Status status) {
                notification = new RadioNotification(getBaseContext(), status.toString())
                        .getNotification();
                NotificationManager mNotificationManager = (NotificationManager) getBaseContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(RadioNotification.ID, notification);
            }
        };
    }
}
