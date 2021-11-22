package com.example.busapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.MapView;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


class ProcessHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
    }
}


public class BusActivity3 extends AppCompatActivity implements OnMapReadyCallback {

    // 변수들
    String hour, min;
    String start,arrival;
    String time;
    String busType;
    // 출발지
    TextView start_txt;
    TextView arrival_txt;
    TextView time_txt;
    
    BusManager busManager;
    String [] MJUSTATION_TIMETABLE;
    int [] MJUSTATION_TIMEREQUIRE;
    String [] MJUSTATION_STATIONS;
    String [] CITY_TIMETABLE;
    int [] CITY_TIMEREQUIRE;
    String [] CITY_STATIONS;

    //버스 출발 시간
    String[] startTimes;
    //현재 시간
    DateFormat currentTime;
    //버스가 도착할 시간
    DateFormat arrivalTime;
    //입력받은 시간
    DateFormat targetTime;
    //버스 도착까지 남은 시간
    int timeLeft;
    //가장 가까운 시내(빨간)버스
    int minCityBus;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus3);
//        ((SlidingUpPanelLayout) findViewById(R.id.bus_sliding_layout)).setAnchorPoint(0.4f);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes her

        }

        MapView mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Log.d("남은 정류장 : ", Arrays.toString(getIntent().getStringArrayExtra("restStation")));


        //데이터 설정
        busManager = new BusManager(getResources().openRawResource(R.raw.businfo));

        MJUSTATION_STATIONS = busManager.BUS_MJUSTATION_STATIONS();
        MJUSTATION_TIMETABLE = busManager.BUS_MJUSTATION_TIMETABLE();
        CITY_STATIONS = busManager.BUS_CITY_STATIONS();
        CITY_TIMETABLE = busManager.BUS_CITY_TIMETABLE();
        // 데이터를 저장합니다.
        Intent intent = getIntent();

        // 버스정류장 저장
        busType = intent.getStringExtra("bus");
        //Toast.makeText(BusActivity3.this, bus, Toast.LENGTH_LONG).show();

        time = intent.getStringExtra("currentTime");
        hour = intent.getStringExtra("hour");
        min = intent.getStringExtra("min");
        if(Integer.parseInt(min)<10){
            min = "0" + min;
        }
        start = intent.getStringExtra("start");
        arrival = intent.getStringExtra("arrival");
        targetTime = new DateFormat(hour + ":" + min);
        currentTime = new DateFormat(time);

        //타겟시간에 대한 가까운 셔틀 버스찾기 : 1차 알고리즘 작성 (메서드 분리 이전, 기능 별 항목 구현)
        //MJUSTATION과 CITYSTATION 모두 포함한다면: 두 버스 모두 지나가는 정류장 (이 때 시간을 정렬해서 새로운 시간표를 만든다)
        if(Search.hasTarget(start, MJUSTATION_STATIONS) && Search.hasTarget(start, CITY_STATIONS)) {
            String [] SORTED_INTEGRATED_TIMETABLE = integrateTimeTable();
            startTimes = Search.FindClosestBus(targetTime.getTime(), SORTED_INTEGRATED_TIMETABLE);
            Log.d("노선", "MJ,CITY");
            //2차 알고리즘 작성: 가까운 셔틀 버스 출발 시간을 이욯하여, start 정류장에 도착할 시간 구하기
            //input: 타겟 시간, 버스 출발 시간(직전 버스, 이후 버스), 정류장, TIME_REQUIRE 배열
            //시작 정류장에서부터, 현재정류장까지 걸리는 시간을 구한다
            //직전 버스 출발 시간에 위에서 구한 시간을 더했을 때, 타겟 시간 후라면 이 시간이 도착예정 시간
            //타겟 시간 전이라면, 이미 버스가 지나간 것이므로, 이후 버스에 대해서 위에서 구한 시간을 더하면 이 시간이 도착예정 시간'

            arrivalTime = compareArrivalTime(start, startTimes, targetTime.getTime());
        }
        //출발지 정류장이 만약 CITYSTATION_STATIONS에만 있다면 : 시내버스만 지나가는 정류장
        else if(Search.hasTarget(start, CITY_STATIONS)){
            startTimes = Search.FindClosestBus(targetTime.getTime(), CITY_TIMETABLE);
            arrivalTime = compareArrivalTime(start, startTimes, targetTime.getTime());
            Log.d("노선", "CITY");
        }
        //출발지 정류장이 만약 MJSTATION_STATIONS에만 있다면: 명지대역버스만 지나가는 정류장
        else{
            startTimes = Search.FindClosestBus(targetTime.getTime(), MJUSTATION_TIMETABLE);
            arrivalTime = compareArrivalTime(start, startTimes, targetTime.getTime());
            Log.d("노선", "MJ");
        }

        //데이터 적용
        start_txt = findViewById(R.id.textView6);
        start_txt.setText(start);

        arrival_txt = findViewById(R.id.textView7);
        arrival_txt.setText(arrival);

        time_txt = findViewById(R.id.textView10);

        /**
         * =======================================================
         *           ^__^ (진입로) 시 내 버 스 연 동 코 드 ^__^
         * =======================================================
         */
        int minCityBus = BusManager.getClosestCityBus();
        // BusManager.predictBusTime("08:00"); --> 08:00
        //가장 가깝게 도착한
        /**
         * =======================================================
         *           ^__^ NaverMap API 연동 코드 ^__^
         * =======================================================
         */

        //3차 알고리즘: 다이얼로그 값에 따라 남은 시간 설정 다르게 하기
        //진입로가 선택됐을 때 다이얼로그 출력: 선택된 값에 따라 timeLeft값 설정
        // timeLeft: 가장 가까운 버스가 남은 시간
        // DateFormat.compare(arrivalTime.getTime(), targetTime.getTime()): 가장 가까운 셔틀버스 남은 시간
        // minCityBus: 가장 가까운 빨간버스 남은 시간
        if(busType!=null){
            switch (busType) {
                case "셔틀버스":
                    Log.d("정류장체크", "진입로 :case 셔틀버스");
                    timeLeft = DateFormat.compare(arrivalTime.getTime(), targetTime.getTime());
                    break;
                case "시내버스":
                    Log.d("정류장체크", "진입로 :case 시내버스");
                    timeLeft = minCityBus;
                    break;
                //null 이거나(진입로외에 다른 정류장), 셔틀버스만 일 때
                case "통합":
                    Log.d("정류장체크", "진입로 :case 통합");
                    //만약 시내버스가 있다면
                    if(minCityBus!=-1){
                        //시내버스와 셔틀버스를 비교해서 더 빠른 것을 반환
                        timeLeft = Math.min(minCityBus, DateFormat.compare(arrivalTime.getTime(), targetTime.getTime()));
                    }
                    //시내버스가 없다면
                    else{
                        //가장 빠른 셔틀버스를 반환
                        timeLeft = DateFormat.compare(arrivalTime.getTime(), targetTime.getTime());
                    }
                    break;
            }
            //null로 초기화
            busType = null;
        }
        //진입로를 제외한 이외의 정류장 일 때
        else{
            Log.d("정류장체크", "이외 정류장");
            timeLeft = DateFormat.compare(arrivalTime.getTime(), targetTime.getTime());
        }

//        Log.d("TEST : ", Arrays.toString(BusManager.getStationRouteInfo("11:55")));
        //끝값 처리: 오늘 버스 모두 끝났을 때
        if(timeLeft<=0){
            AlertDialog.Builder myAlBuilder=
                    new AlertDialog.Builder(BusActivity3.this);
            myAlBuilder.setMessage("금일 버스가 모두 운행 종료되었습니다.");
            myAlBuilder.setPositiveButton("이전으로", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                }
            });
            myAlBuilder.show();
        }
        else{
            time_txt.setText(timeLeft + "분");
        }

        //테스트 데이터 출력
        Log.d("타겟시간", targetTime.getTime());
        Log.d("내가 탈 정류장", start);
        Log.d("출발시간", Arrays.toString(startTimes));
        Log.d("도착시간", arrivalTime.getTime());
        Log.d("명지대역 버스 정류장별 예상 소요 시간", Arrays.toString(MJUSTATION_TIMEREQUIRE));
        Log.d("시내(셔틀) 버스 정류장병 예상 소요 시간", Arrays.toString(CITY_TIMEREQUIRE));

    }



    //시간표 통합 메서드
    //두 시간표를 통합하여 시간별로 sort후, 반환한다.s
    public String[] integrateTimeTable(){
        List<String> list1 = new ArrayList<>(Arrays.asList(MJUSTATION_TIMETABLE));
        List<String> list2 = new ArrayList<>(Arrays.asList(CITY_TIMETABLE));
        list1.addAll(list2);
        //시내버스, 명지대역 통합 버스 시간표
        String [] integrateTime = list1.toArray(new String[0]);
        String [] sortedIntegrateTime = QuickSort.sort(integrateTime, DateFormat::compare);

        return sortedIntegrateTime;
    }

    //도착 시간 구하기 메서드
    //INPUT: 도착 정류장, 버스 출발시간
    //도착 정류장이 어떤 TIMETABLE에 속해있는지 판단 한 후, 해당 노선도의 TIMEREQUIRE테이블을 이용해서 도착 정류장에 버스가 도착할 시간을 반환한다.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public DateFormat getArrivalTime(String startStation, String startTime){
        DateFormat arrivalTime = new DateFormat(startTime);

        int stationIndex = 0;
        //버스가 명지대역 버스라면 : 이진 탐색
        if(Search.hasTarget(startTime, MJUSTATION_TIMETABLE)){
            //현재 시간 != 타겟시간이면
            if(currentTime.totalMin!=targetTime.totalMin){
                //해당 시간에 예상되는 명지대역 정류장별 소요 시간을 배열에 담는다.
                MJUSTATION_TIMEREQUIRE = BusManager.predictShuttleTime(startTime);
            }
            //현재 시간 = 타겟시간이면
            else{
                //현재 시간에 예상되는 정류장별 소요시간을 배열에 담는다.
                MJUSTATION_TIMEREQUIRE = BusManager.getStationRouteInfo();
            }

            for (int i = 0; i < MJUSTATION_STATIONS.length; i++) {
                if (MJUSTATION_STATIONS[i].equals(startStation)) {
                    stationIndex = i;
                }
            }
            //구해진 stationIndex까지 즉 출발점부터 start 정류장까지 걸리는 시간을 더한다.
            for(int i =0; i<=stationIndex; i++){
                arrivalTime.addTime(MJUSTATION_TIMEREQUIRE[i]);
            }
        }
        //버스가 시내 버스라면
        else {
            //현재 시간 != 타겟시간이면
            if(currentTime.totalMin!=targetTime.totalMin){
                //해당 시간에 예상되는 시내(셔틀) 정류장별 소요 시간을 배열에 담는다.
                CITY_TIMEREQUIRE = BusManager.predictShuttleTime(startTime);
            }
            //현재 시간 = 타겟시간이면
            else{
                //현재 시간에 예상되는 시내(셔틀) 소요시간을 배열에 담는다.
                CITY_TIMEREQUIRE = BusManager.getCityRouteInfo();
            }
            for (int i = 0; i < CITY_STATIONS.length; i++) {
                if (CITY_STATIONS[i].equals(start)) {
                    stationIndex = i;
                }
            }
            for(int i =0; i<=stationIndex; i++){
                arrivalTime.addTime(CITY_TIMEREQUIRE[i]);
            }
        }
        return arrivalTime;
    }

    //도착 버스 시간 비교 메서드
    //INPUT : 도착 정류장, 2개의 버스출발시간(타겟시간 이전버스, 타겟시간 이후버스)
    //직전 버스, 이후 버스 중 어떤 버스가 먼저 도착할 지 판단한 후 먼저 도착하는 버스의 도착시간을 반환한다.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public DateFormat compareArrivalTime(String startStation, String [] startTimes, String targetTime){

        DateFormat arrivalTime;

        DateFormat bus1ArrivalTime = getArrivalTime(startStation, startTimes[0]);
        DateFormat bus2ArrivalTime = getArrivalTime(startStation, startTimes[1]);

        //이전에 출발한 버스 시간 > 타겟 시간 : 아직 도착 전이기 때문에 ArrivalTime은 이전에 출발한 버스가 도착할 시간이 된다.
        if(DateFormat.compare(bus1ArrivalTime.getTime(), targetTime) > 0){
            arrivalTime = bus1ArrivalTime;
        }
        //이전에 출발한 버스 시간 < 타겟 시간 : ArrivalTime은 이후에 출발한 버스가 도착할 시간이 된다.
        else{
            arrivalTime = bus2ArrivalTime;
        }
        return arrivalTime;
    }

    public void setAlarm(View view) {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(BusActivity3.this, BusAlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BusActivity3.this, 0, receiverIntent, 0);

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

        Calendar targetAlarmTime = Calendar.getInstance();
        targetAlarmTime.setTime(new Date());
        targetAlarmTime.add(Calendar.MINUTE, (arrivalTime.getTotalMin() - targetTime.getTotalMin()) - 5);
        Log.d("D/알림 설정 : ", dateFormat.format(targetAlarmTime.getTime()));

        Date datetime = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                datetime = dateFormat.parse(dateFormat.format(targetAlarmTime.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }

    // 지도 코드
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        MapMarkerManager mapMarkerManager = new MapMarkerManager(naverMap);
        MapPolyManager mapPolyManager = new MapPolyManager(naverMap);
        if(Search.hasTarget(start, MJUSTATION_STATIONS)) {
            mapMarkerManager.setMarkers(false, true);
            mapPolyManager.setPolyLine(false, true);
        } else {
            mapMarkerManager.setMarkers(true, false);
            mapPolyManager.setPolyLine(true, false);
        }
        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                mapMarkerManager.getPosition(start),15)
                .animate(CameraAnimation.Fly, 3000);
        naverMap.moveCamera(cameraUpdate);
        naverMap.setLocationSource(new FusedLocationSource(this, 1000));

        // UI 재배치
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(false);
        LocationButtonView locationButtonView = findViewById(R.id.naverMap_location);
        locationButtonView.setMap(naverMap);

    }
}