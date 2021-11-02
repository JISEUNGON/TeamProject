package com.example.busapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePicker.OnTimeChangedListener{

    //////////////변수들//////////////
    // 타임피커
    TimePicker timePicker;
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

        // 출발지 스피너 탐색
        sp1 = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list01, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(this);


        // 도착지 스피너 탐색
        sp2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list03, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(this);

        // 타임피커 설정
        timePicker = findViewById(R.id.timepicker);
        // 초기 시간, 분 설정
        timePicker.setCurrentHour(12);
        timePicker.setCurrentMinute(55);

        // canlendar설정
        calendar = Calendar.getInstance();

    }

    private void setNumberPickerTextColor(View spinner, int color) {
        int mNumberPickerInputId = Resources.getSystem().getIdentifier("numberpicker_input", "id", "android");
        TextView input = (TextView) spinner.findViewById(mNumberPickerInputId);
        input.setTextColor(color);
    }

    // 버스찾기 버튼 누른후
    public void SearchBus(View v){

        // 문자열에 시간, 출발지, 도착지를 저장합니다.

        // 시간 저장
        hour_str = timePicker.getCurrentHour().toString();
        // 분저장
        min_str = timePicker.getCurrentMinute().toString();
        // 출발지 값 저장
        start_str = sp1.getSelectedItem().toString();
        // 도착지 값 저장
        aririval_str = sp2.getSelectedItem().toString();

        if(start_str.equals("정류장")){

        }
        else {
            // intent에 데이터를 저장합니다.
            Intent intent = new Intent(this, BusActivity3.class);
            intent.putExtra("hour", hour_str);
            intent.putExtra("min", min_str);
            intent.putExtra("start", start_str);
            intent.putExtra("arrival", aririval_str);

            // 화면 전환합니다
            startActivity(intent);
        }
    }

    public void showBusInfo(View v) {
        startActivity(new Intent(this, BusInfoActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // 글씨 흰색으로 변경
    //    ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        //h\\ = String.valueOf(hourOfDay);
        //m = String.valueOf(minute);

        ((TextView)view.getChildAt(0)).setTextColor(Color.WHITE);


    }

    // 현재시간으로 설정
    public void setCurrTime(View v){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

    }

    // 추후 현재 위치 버튼으로 연동할 부분
    public void setCurrLocation(View v) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){ //포그라운드 위치 권한 확인
            //위치 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

    }

}