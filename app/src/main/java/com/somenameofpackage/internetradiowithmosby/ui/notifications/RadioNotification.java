package com.somenameofpackage.internetradiowithmosby.ui.notifications;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.somenameofpackage.internetradiowithmosby.R;
import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import com.somenameofpackage.internetradiowithmosby.ui.RadioActivity;

import static com.somenameofpackage.internetradiowithmosby.model.radio.RadioService.ACTION;
import static com.somenameofpackage.internetradiowithmosby.model.radio.RadioService.PLAY;

public class RadioNotification {
    private final Notification notification;

    public RadioNotification(Context context) {
        this(context, "pause");
    }

    public RadioNotification(Context context, String text) {
        Log.v("gGG", "123");
        Intent playRadioIntent = new Intent(context, RadioService.class);
        playRadioIntent.putExtra(ACTION, PLAY);

        PendingIntent playPendingIntent = PendingIntent.getService(context, 22, playRadioIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentTitle("Hello")
                        .setContentText("source")
                        .addAction(R.mipmap.ic_launcher, text, playPendingIntent)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, RadioActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RadioActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        notification = mBuilder.build();
    }

    public Notification getNotification() {
        return notification;
    }
}
