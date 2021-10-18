package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 시작하기 버튼 클릭시 메인화면으로 이동
    public void StartBt(View v){
        // 화면 전환합니다
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(intent);

    }
}