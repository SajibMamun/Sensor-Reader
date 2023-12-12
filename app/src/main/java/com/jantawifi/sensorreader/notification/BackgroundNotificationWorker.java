package com.jantawifi.sensorreader.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.sensor.SensorReader;

public class BackgroundNotificationWorker extends Worker {

    public BackgroundNotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get the latest sensor data
        SensorReader sensorReader = new SensorReader(getApplicationContext());
        float latestLightValue = sensorReader.getLightSensorValue();
        float latestProximityValue = sensorReader.getProximitySensorValue();
        float latestAccelerometerValue = sensorReader.getAccelerometerValue();
        float latestGyroscopeValue = sensorReader.getGyroscopeValue();

        // Create and show a notification with the latest sensor data
       showNotification("Sensor Data Notification", buildNotificationMessage(latestLightValue, latestProximityValue, latestAccelerometerValue, latestGyroscopeValue));

        return Result.success();
    }

    private void showNotification(String title, String message) {
        // Create a notification channel (for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "background_notification_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "background_notification_channel",
                    "Background Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
    private String buildNotificationMessage(float lightValue, float proximityValue, float accelerometerValue, float gyroscopeValue) {
        return "Latest Sensor Data:\n" +
                "Light Sensor: " + lightValue + "\n" +
                "Proximity Sensor: " + proximityValue + "\n" +
                "Accelerometer: " + accelerometerValue + "\n" +
                "Gyroscope: " + gyroscopeValue;
    }
}
