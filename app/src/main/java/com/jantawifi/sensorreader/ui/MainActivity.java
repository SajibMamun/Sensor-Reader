package com.jantawifi.sensorreader.ui;

import static androidx.core.app.ServiceCompat.stopForeground;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.database.SQLiteHelper;
import com.jantawifi.sensorreader.databinding.ActivityMainBinding;
import com.jantawifi.sensorreader.notification.BackgroundNotificationScheduler;
import com.jantawifi.sensorreader.sensor.SensorDataListener;
import com.jantawifi.sensorreader.sensor.SensorReader;
import com.jantawifi.sensorreader.sensor.SensorService;
import com.jantawifi.sensorreader.sensor.SensorWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorDataListener {

    private ActivityMainBinding binding;

    private TextView lightValue, proximityValue, accelerometerValue, gyroscopeValue;
    private SensorReader sensorReader;
    private SQLiteHelper dbHelper;

    private static final String SENSOR_WORK_TAG = "sensor_work_tag";
    private static final int SENSOR_SERVICE_REQUEST_CODE = 101;
    private static final int NOTIFICATION_ID = 1;
    private BroadcastReceiver appBackgroundReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        lightValue =binding.lightValue;
        proximityValue = binding.proximityValue;
        accelerometerValue = binding.accelerometerValue;
        gyroscopeValue = binding.gyroscopeValue;

        // Initialize SensorReader
        sensorReader = new SensorReader(this);
        sensorReader.addSensorDataListener(this);

        // Initialize database helper
        dbHelper = new SQLiteHelper(this);

        // Register click listeners for sensor cards
        CardView lightCard = findViewById(R.id.cardLight);
        CardView proximityCard = findViewById(R.id.cardProximity);
        CardView accelerometerCard = findViewById(R.id.cardAccelerometer);
        CardView gyroscopeCard = findViewById(R.id.cardGyroscope);

        lightCard.setOnClickListener(view -> openChartActivity(Sensor.TYPE_LIGHT));
        proximityCard.setOnClickListener(view -> openChartActivity(Sensor.TYPE_PROXIMITY));
        accelerometerCard.setOnClickListener(view -> openChartActivity(Sensor.TYPE_ACCELEROMETER));
        gyroscopeCard.setOnClickListener(view -> openChartActivity(Sensor.TYPE_GYROSCOPE));

        // Schedule the periodic sensor check using WorkManager
        scheduleSensorWorker();
        // Start background service
       startBackgroundService();


        //Register BroadcastReceiver to detect app background
        registerAppBackgroundReceiver();


    }

    private void scheduleSensorWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest sensorWork =
                new PeriodicWorkRequest.Builder(SensorWorker.class, 5, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(sensorWork);
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister SensorService when the activity is destroyed
        stopSensorService();

        // Unregister BroadcastReceiver
        unregisterReceiver(appBackgroundReceiver);
    }

    private void startSensorService() {
        Intent serviceIntent = new Intent(this, SensorService.class);
        startService(serviceIntent);
    }

    private void stopSensorService() {
        Intent serviceIntent = new Intent(this, SensorService.class);
        stopService(serviceIntent);
    }

    private void registerAppBackgroundReceiver() {
        appBackgroundReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    // The screen is turned off, indicating that the app is in the background

                    // Schedule the background notification task using WorkManager
                    BackgroundNotificationScheduler.scheduleBackgroundNotification(context);
                } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    // The screen is turned on, indicating that the app is in the foreground

                    // Cancel the scheduled background notification task
                    BackgroundNotificationScheduler.cancelBackgroundNotification(context);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(appBackgroundReceiver, filter);
    }

    private void startBackgroundService() {
        // Schedule periodic work
        PeriodicWorkRequest sensorWork = new PeriodicWorkRequest.Builder(
                SensorWorker.class,
                5, TimeUnit.MINUTES // Change the interval as needed
        ).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                SENSOR_WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                sensorWork
        );
    }



    @Override
    public void onSensorDataChanged(int sensorType, float value) {
        runOnUiThread(() -> {
            // Update UI based on sensor type
            switch (sensorType) {
                case Sensor.TYPE_LIGHT:
                    lightValue.setText("Light Sensor: " + value);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    proximityValue.setText("Proximity Sensor: " + value);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValue.setText("Accelerometer: " + value);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    gyroscopeValue.setText("Gyroscope: " + value);
                    break;
            }
        });
    }








    private void openChartActivity(int sensorType) {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("sensorType", sensorType);
        startActivity(intent);
    }



}