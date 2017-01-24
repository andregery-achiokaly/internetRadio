package com.somenameofpackage.internetradiowithmosby.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.somenameofpackage.internetradiowithmosby.model.radio.RadioService;
import static android.support.v4.app.ServiceCompat.START_STICKY;

public class PlayerUtil {
    public static void initPlayerService(final ServiceConnection serviceConnection, Context context) {
        startService(context);
        bindToService(serviceConnection, context);
    }

    private static void startService(Context context) {
        Intent intent = new Intent(context, RadioService.class);
        intent.addFlags(START_STICKY);
        context.startService(intent);
    }

    private static void bindToService(final ServiceConnection serviceConnection, Context context) {
        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, 0);
    }

    static void stopService(Context context) {
        Intent intent = new Intent(context, RadioService.class);
        context.stopService(intent);
    }
}
