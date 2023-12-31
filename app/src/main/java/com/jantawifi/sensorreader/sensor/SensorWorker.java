package com.jantawifi.sensorreader.sensor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.database.SQLiteHelper;

public class SensorWorker extends Worker {
    private SensorReader sensorReader;
    private SQLiteHelper dbHelper;

    public SensorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sensorReader = new SensorReader(context);
        dbHelper = new SQLiteHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        float lightValue = sensorReader.getLightSensorValue();
        float proximityValue = sensorReader.getProximitySensorValue();
        float accelerometerValue = sensorReader.getAccelerometerValue();
        float gyroscopeValue = sensorReader.getGyroscopeValue();

        dbHelper.saveSensorData(lightValue, proximityValue, accelerometerValue, gyroscopeValue);


        // Notify the user with the latest data
        showNotification("Sensor Data Notification", buildNotificationMessage(
                lightValue, proximityValue, accelerometerValue, gyroscopeValue));

        return Result.success();
    }



    private void showNotification(String title, String message) {
        // Create a notification channel (for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "sensor_notification_channel")
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
                    "sensor_notification_channel",
                    "Sensor Notification Channel",
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