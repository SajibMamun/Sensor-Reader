package com.jantawifi.sensorreader.sensor;

public interface SensorDataListener {
    void onSensorDataChanged(int sensorType, float value);
}