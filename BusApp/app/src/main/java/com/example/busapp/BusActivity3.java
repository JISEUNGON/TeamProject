package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BusActivity3 extends AppCompatActivity {

    // 변수들

    String hour, min;
    String start,arrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus3);

        Intent intent = getIntent();

        hour = intent.getStringExtra("hour");
        min = intent.getStringExtra("min");
        start = intent.getStringExtra("start");
        arrival = intent.getStringExtra("arrival");

        Toast.makeText(this, hour, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, min, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, start, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, arrival, Toast.LENGTH_SHORT).show();



        //hour = activity_main2.get
    }

}