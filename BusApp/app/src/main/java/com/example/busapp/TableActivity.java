package com.example.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
    }

    // 노선도 버튼 클릭후
    public void ViewBusLine(View v){
        Intent intent = new Intent(this, BusInfoActivity.class);
        // 화면 전환합니다
        startActivity(intent);
    }
}