package com.jantawifi.sensorreader.Recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jantawifi.sensorreader.R;
import com.jantawifi.sensorreader.sensor.SensorData;

import java.util.List;

public class SensorDataAdapter extends RecyclerView.Adapter<SensorDataAdapter.SensorDataViewHolder> {

    public List<SensorData> sensorDataList;
    private String sensorTypeToShow;

    public SensorDataAdapter(List<SensorData> sensorDataList, String sensorTypeToShow) {
        this.sensorDataList = sensorDataList;
        this.sensorTypeToShow = sensorTypeToShow;
    }

    @NonNull
    @Override
    public SensorDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutdesign, parent, false);
        return new SensorDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDataViewHolder holder, int position) {

        SensorData sensorData = sensorDataList.get(position);

        if ("5".equals(sensorTypeToShow)) {
            holder.sensorvalue.setText(String.valueOf(sensorData.getLightValue()));
            holder.timestamp.setText(""+sensorData.getTimestamp());

        } else if ("8".equals(sensorTypeToShow)) {
            holder.sensorvalue.setText(String.valueOf(sensorData.getProximityValue()));
            holder.timestamp.setText(""+sensorData.getTimestamp());
        }
        else if ("1".equals(sensorTypeToShow)) {
            holder.sensorvalue.setText(String.valueOf(sensorData.getAccelerometerValue()));
            holder.timestamp.setText(""+sensorData.getTimestamp());
        }
        else if ("4".equals(sensorTypeToShow)) {
            holder.sensorvalue.setText(String.valueOf(sensorData.getGyroscopeValue()));
            holder.timestamp.setText(""+sensorData.getTimestamp());
        }

    }

    @Override
    public int getItemCount() {
        return sensorDataList.size();
    }


    static class SensorDataViewHolder extends RecyclerView.ViewHolder {
        TextView sensorvalue,timestamp;

        public SensorDataViewHolder(@NonNull View itemView) {
            super(itemView);

            sensorvalue = itemView.findViewById(R.id.SensorValueTv);
            timestamp = itemView.findViewById(R.id.TimevalueTv);

        }
    }}