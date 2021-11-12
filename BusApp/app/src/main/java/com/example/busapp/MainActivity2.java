package com.example.busapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePicker.OnTimeChangedListener{

    //////////////변수들//////////////
    // 타임피커
    TimePicker timePicker;
    Calendar calendar;

    // 스피너
    Spinner sp1;
    Spinner sp2;

    // 도착지, 출발지, 시간
    String start_str, arrival_str, hour_str, min_str;
    // 버스 종류
    String bus_str;

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

        // 시작할때 시내버스 값을 저장합니다.
        bus_str = "시내버스";

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
        arrival_str = sp2.getSelectedItem().toString();

        // 정류장을 선택했을 경우입니다.
        if(start_str.equals("정류장")){

        }
        else {
            // intent에 데이터를 저장합니다.
            Intent intent = new Intent(this, BusActivity3.class);
            intent.putExtra("hour", hour_str);
            intent.putExtra("min", min_str);
            intent.putExtra("start", start_str);
            intent.putExtra("arrival", arrival_str);

            // 진입로를 선택했을 경우입니다.
            // 대화상자가 보여집니다.
            if(start_str.equals("진입로")){

                // 버스 목록입니다.
                String[] buss_list = new String[] {"시내버스", "셔틀버스", "셔틀버스,시내버스 통합"};

                // 다이얼로그 생성
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity2.this);
                // 제복부분
                dlg.setTitle("버스를 선택해 주세요");

                // 라디오버튼 부분
                dlg.setSingleChoiceItems(buss_list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //button1.setText(buss_list[which]);

                        // 어느 버스 선택했는지 확인합니다.
                        if(buss_list[which] == "시내버스"){
                            bus_str = "시내버스";
                            //Toast.makeText(MainActivity2.this, "시내버스", Toast.LENGTH_LONG).show();
                        }
                        else if(buss_list[which] == "셔틀버스"){
                            bus_str = "셔틀버스";
                            //Toast.makeText(MainActivity2.this, "셔틀버스", Toast.LENGTH_LONG).show();
                        }
                        else{
                            bus_str = "통합";
                        }
                    }
                });

                // 확인버튼 누른 경우입니다.
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // intent에 데이터를 저장합니다.
                        intent.putExtra("bus", bus_str);

                        // 화면 전환합니다
                        startActivity(intent);
                    }
                });

                // 취소버튼을 누른 경우입니다.
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });



                dlg.show();
            }
            else{
                // 화면 전환합니다
                startActivity(intent);
            }
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

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            sp1.setSelection(getIndex(Search.FindClosestStation(location)), true);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, location1 -> sp1.setSelection(getIndex(Search.FindClosestStation(location1)), true));
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, location1 -> sp1.setSelection(getIndex(Search.FindClosestStation(location1)), true));

        } catch (Exception e) {
            Log.d("setCurrLocation", e.getMessage());
        }
    }

    private int getIndex(String station) {
        String[] stations = getResources().getStringArray(R.array.list01);
        for(int i=0; i<stations.length; i++)
            if(station.equals(stations[i])) return i;
        return -1;
    }

}