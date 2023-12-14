package com.jantawifi.sensorreader.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jantawifi.sensorreader.sensor.SensorData;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sensor_data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SENSOR_DATA = "sensor_data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LIGHT_VALUE = "light_value";
    private static final String COLUMN_PROXIMITY_VALUE = "proximity_value";
    private static final String COLUMN_ACCELEROMETER_VALUE = "accelerometer_value";
    private static final String COLUMN_GYROSCOPE_VALUE = "gyroscope_value";
    private static final String COLUMN_TIMESTAMP = "timestamp";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            String createTableQuery = "CREATE TABLE " + TABLE_SENSOR_DATA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LIGHT_VALUE + " REAL, " +
                    COLUMN_PROXIMITY_VALUE + " REAL, " +
                    COLUMN_ACCELEROMETER_VALUE + " REAL, " +
                    COLUMN_GYROSCOPE_VALUE + " REAL, " +
                    COLUMN_TIMESTAMP + " INTEGER)"; // Add the new timestamp column
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }

    public void saveSensorData(float lightValue, float proximityValue, float accelerometerValue, float gyroscopeValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIGHT_VALUE, lightValue);
        values.put(COLUMN_PROXIMITY_VALUE, proximityValue);
        values.put(COLUMN_ACCELEROMETER_VALUE, accelerometerValue);
        values.put(COLUMN_GYROSCOPE_VALUE, gyroscopeValue);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Add timestamp
        db.insert(TABLE_SENSOR_DATA, null, values);
        db.close();
    }




    @SuppressLint("Range")
    public List<SensorData> getAllSensorData() {
        List<SensorData> sensorDataList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SENSOR_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                SensorData sensorData = new SensorData();
                sensorData.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                sensorData.setLightValue(cursor.getFloat(cursor.getColumnIndex(COLUMN_LIGHT_VALUE)));
                sensorData.setProximityValue(cursor.getFloat(cursor.getColumnIndex(COLUMN_PROXIMITY_VALUE)));
                sensorData.setAccelerometerValue(cursor.getFloat(cursor.getColumnIndex(COLUMN_ACCELEROMETER_VALUE)));
                sensorData.setGyroscopeValue(cursor.getFloat(cursor.getColumnIndex(COLUMN_GYROSCOPE_VALUE)));
                sensorData.setTimestamp(cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP)));

                // Add sensorData to the list
                sensorDataList.add(sensorData);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return sensorDataList;
    }
}



