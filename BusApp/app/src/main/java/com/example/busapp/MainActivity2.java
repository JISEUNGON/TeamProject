package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePicker.OnTimeChangedListener{

    //////////////변수들//////////////

    // 라디오 버튼 목록
    Button city_bt;
    Button mju_bt;

    // 타임피커
    TimePicker timepicker;
    Calendar calendar;

    // 스피너
    Spinner sp1;
    Spinner sp2;

    // 도착지, 출발지, 시간
    String start_str,aririval_str, hour_str, min_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 버튼 탐색 후 버튼 지정
        city_bt =  findViewById(R.id.radioButton4);
        mju_bt =  findViewById(R.id.radioButton5);
        city_bt.setSelected(true);
        mju_bt.setSelected(false);

        // 출발지 스피너 탐색
        sp1 = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list01, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(this);

        // 도착지 스피너 탐색
        sp2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list01, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(this);

        // 타임피커 설정
        timepicker = findViewById(R.id.timepicker);
        // 초기 시간, 분 설정
        timepicker.setCurrentHour(12);
        timepicker.setCurrentMinute(55);

        // canlendar설정
        calendar = Calendar.getInstance();

    }

    // 버스찾기 버튼 누른후
    public void SearchBus(View v){

        // 문자열에 시간, 출발지, 도착지를 저장합니다.

        // 시간 저장
        hour_str = timepicker.getCurrentHour().toString();
        // 분저장
        min_str = timepicker.getCurrentMinute().toString();
        // 출발지 값 저장
        start_str = sp1.getSelectedItem().toString();
        // 도착지 값 저장
        aririval_str = sp2.getSelectedItem().toString();

        // intent에 데이터를 저장합니다.
        Intent intent = new Intent(this, BusActivity3.class);
        intent.putExtra("hour", hour_str);
        intent.putExtra("min", min_str);
        intent.putExtra("start", start_str);
        intent.putExtra("arrival", aririval_str);

        // 화면 전환합니다
        startActivity(intent);
    }

    // 시내버스 버튼 클릭후
    public void SelectCityBus(View v){
        // 출발지 스피너 탐색
        Spinner sp1 = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list01, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(this);

        // 도착지 스피너 탐색
        Spinner sp2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list01, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(this);

        // 버튼이미지 변경
        city_bt.setSelected(true);
        mju_bt.setSelected(false);
    }

    // 명지대역 버스 클릭후
    public void SelectMjBus(View V){

        // 출발지 스피너 탐색
        Spinner sp1 = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list02, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(this);

        // 도착지 스피너 탐색
        Spinner sp2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list02, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(this);

        // 버튼 이미지 변경
        city_bt.setSelected(false);
        mju_bt.setSelected(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        //h\\ = String.valueOf(hourOfDay);
        //m = String.valueOf(minute);


    }

    // 현재시간으로 설정
    public void setCurrTime(View v){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        timepicker.setCurrentHour(hour);
        timepicker.setCurrentMinute(min);

    }
}