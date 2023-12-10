package com.jantawifi.sensorreader.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private TextView lightValue, proximityValue, accelerometerValue, gyroscopeValue;
    private SensorManager sensorManager;
    private Sensor lightSensor, proximitySensor, accelerometerSensor, gyroscopeSensor;



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


        lightValue = binding.lightValue;
        proximityValue = binding.proximityValue;
        accelerometerValue = binding.accelerometerValue;
        gyroscopeValue = binding.gyroscopeValue;

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }



    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listeners
        sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners to save battery
        sensorManager.unregisterListener(sensorListener);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float value = event.values[0];
            long timestamp = System.currentTimeMillis();

            switch (event.sensor.getType()) {
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

        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used in this example
        }
    };

    private void startBackgroundService() {
        // TODO: Implement background service
    }

}