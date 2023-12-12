package com.jantawifi.sensorreader.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppBackgroundReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            // The screen is turned off, indicating that the app is in the background

            // Schedule the background notification task using WorkManager
            BackgroundNotificationScheduler.scheduleBackgroundNotification(context);
        }
    }
}