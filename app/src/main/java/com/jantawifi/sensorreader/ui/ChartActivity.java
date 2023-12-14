package com.jantawifi.sensorreader.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.hardware.Sensor;
import android.os.Bundle;

import android.widget.Toast;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.database.SQLiteHelper;
import com.jantawifi.sensorreader.databinding.ActivityChartBinding;
import com.jantawifi.sensorreader.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private ActivityChartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}