package com.jantawifi.sensorreader.notification;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BackgroundNotificationScheduler {

    private static final String WORK_TAG = "background_notification_work";

    public static void scheduleBackgroundNotification(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest notificationWorkRequest =
                new OneTimeWorkRequest.Builder(BackgroundNotificationWorker.class)
                        .setConstraints(constraints)
                        .setInitialDelay(5, TimeUnit.MINUTES)
                        .addTag(WORK_TAG) // Assign a tag to identify this work request
                        .build();

        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
    }

    public static void cancelBackgroundNotification(Context context) {
        // Cancel the background notification work request using its tag
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG);
    }
}