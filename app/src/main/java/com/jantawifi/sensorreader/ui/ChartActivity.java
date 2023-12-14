package com.jantawifi.sensorreader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.hardware.Sensor;
import android.os.Bundle;

import android.widget.Toast;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.Recyclerview.SensorDataAdapter;
import com.jantawifi.sensorreader.database.SQLiteHelper;
import com.jantawifi.sensorreader.databinding.ActivityChartBinding;
import com.jantawifi.sensorreader.databinding.ActivityMainBinding;
import com.jantawifi.sensorreader.sensor.SensorData;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private ActivityChartBinding binding;
    private RecyclerView recyclerView;
    private SensorDataAdapter sensorDataAdapter;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get sensor type from intent
        String sensorType = String.valueOf(getIntent().getIntExtra("sensorType",0));
       CheckSensorType(sensorType);

       recyclerView=binding.Recyclerviewid;
        // Initialize SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        // Fetch all sensor data from the database
        List<SensorData> allSensorData = dbHelper.getAllSensorData();

        // Update RecyclerView with the fetched data
        sensorDataAdapter=new SensorDataAdapter(allSensorData,sensorType);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sensorDataAdapter);








    }

    private void CheckSensorType(String sensorType) {
        if(sensorType.equals("5")){
            binding.SensorNameTv.setText("Light Sensor");

        }
        else if(sensorType.equals("8")){
            binding.SensorNameTv.setText("Proximity Sensor");

        }
        else if(sensorType.equals("1")){
            binding.SensorNameTv.setText("Accelerometer Sensor");

        }
        else if(sensorType.equals("4")){
            binding.SensorNameTv.setText("GyroScope");

        }
    }
}