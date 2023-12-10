package com.jantawifi.sensorreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sensor_data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "sensor_values";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SENSOR_TYPE = "sensor_type";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_VALUE = "value";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SENSOR_TYPE + " INTEGER," +
                    COLUMN_TIMESTAMP + " INTEGER," +
                    COLUMN_VALUE + " REAL);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(int sensorType, long timestamp, float value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SENSOR_TYPE, sensorType);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_VALUE, value);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getData(int sensorType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_SENSOR_TYPE, COLUMN_TIMESTAMP, COLUMN_VALUE};
        String selection = COLUMN_SENSOR_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(sensorType)};
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
}