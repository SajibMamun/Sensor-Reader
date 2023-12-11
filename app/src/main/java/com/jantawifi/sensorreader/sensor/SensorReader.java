package com.jantawifi.sensorreader.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class SensorReader implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;

    private float latestLightValue = 0;
    private float latestProximityValue = 0;
    private float latestAccelerometerValue = 0;
    private float latestGyroscopeValue = 0;
    private List<SensorDataListener> listeners = new ArrayList<>();

    public SensorReader(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Initialize sensors
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register listeners
        registerSensorListeners();
    }

    public void registerSensorListeners() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float getLightSensorValue() {
        return latestLightValue;
    }

    public float getProximitySensorValue() {
        return latestProximityValue;
    }

    public float getAccelerometerValue() {
        return latestAccelerometerValue;
    }

    public float getGyroscopeValue() {
        return latestGyroscopeValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = event.values[0];

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                latestLightValue = value;
                notifyListeners(Sensor.TYPE_LIGHT, value);
                break;
            case Sensor.TYPE_PROXIMITY:
                latestProximityValue = value;
                notifyListeners(Sensor.TYPE_PROXIMITY, value);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                latestAccelerometerValue = value;
                notifyListeners(Sensor.TYPE_ACCELEROMETER, value);
                break;
            case Sensor.TYPE_GYROSCOPE:
                latestGyroscopeValue = value;
                notifyListeners(Sensor.TYPE_GYROSCOPE, value);
                break;
        }

        // You might want to notify listeners or perform additional actions here

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    public void unregisterListeners() {
        // Unregister sensor listeners when they are no longer needed
        sensorManager.unregisterListener(this);
    }

    public void addSensorDataListener(SensorDataListener listener) {
        listeners.add(listener);
    }

    public void removeSensorDataListener(SensorDataListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(int sensorType, float value) {
        for (SensorDataListener listener : listeners) {
            listener.onSensorDataChanged(sensorType, value);
        }
    }
}