package com.jantawifi.sensorreader.sensor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

        return Result.success();
    }
}