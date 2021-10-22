package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BusActivity3 extends AppCompatActivity {

    // 변수들

    String hour, min;
    String start,arrival;

    // 출발지
    TextView start_txt;
    TextView arrival_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus3);

        Intent intent = getIntent();

        hour = intent.getStringExtra("hour");
        min = intent.getStringExtra("min");
        start = intent.getStringExtra("start");
        arrival = intent.getStringExtra("arrival");

        start_txt = findViewById(R.id.textView6);
        start_txt.setText(start);

        arrival_txt = findViewById(R.id.textView7);
        arrival_txt.setText(arrival);

    }

}