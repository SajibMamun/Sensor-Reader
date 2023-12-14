package com.jantawifi.sensorreader.sensor;

public class SensorData {
    private long id;
    private float lightValue;
    private float proximityValue;
    private float accelerometerValue;
    private float gyroscopeValue;
    private long timestamp; // New field for timestamp

    // Constructors, getters, and setters

    public SensorData() {
        // Default constructor
    }

    public SensorData(long id, float lightValue, float proximityValue, float accelerometerValue, float gyroscopeValue, long timestamp) {
        this.id = id;
        this.lightValue = lightValue;
        this.proximityValue = proximityValue;
        this.accelerometerValue = accelerometerValue;
        this.gyroscopeValue = gyroscopeValue;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getLightValue() {
        return lightValue;
    }

    public void setLightValue(float lightValue) {
        this.lightValue = lightValue;
    }

    public float getProximityValue() {
        return proximityValue;
    }

    public void setProximityValue(float proximityValue) {
        this.proximityValue = proximityValue;
    }

    public float getAccelerometerValue() {
        return accelerometerValue;
    }

    public void setAccelerometerValue(float accelerometerValue) {
        this.accelerometerValue = accelerometerValue;
    }

    public float getGyroscopeValue() {
        return gyroscopeValue;
    }

    public void setGyroscopeValue(float gyroscopeValue) {
        this.gyroscopeValue = gyroscopeValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

