package com.jantawifi.sensorreader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.database.SQLiteHelper;
import com.jantawifi.sensorreader.databinding.ActivityMainBinding;
import com.jantawifi.sensorreader.sensor.SensorDataListener;
import com.jantawifi.sensorreader.sensor.SensorReader;
import com.jantawifi.sensorreader.sensor.SensorWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorDataListener {

    private ActivityMainBinding binding;

    private TextView lightValue, proximityValue, accelerometerValue, gyroscopeValue;
    private SensorReader sensorReader;
    private SQLiteHelper dbHelper;

    private static final String SENSOR_WORK_TAG = "sensor_work_tag";


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

        // Start background service
        startBackgroundService();




    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listeners
        sensorReader.registerSensorListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners to save battery
        sensorReader.unregisterListeners();
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

    private void openChartActivity(int sensorType) {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("sensorType", sensorType);
        startActivity(intent);
    }



}