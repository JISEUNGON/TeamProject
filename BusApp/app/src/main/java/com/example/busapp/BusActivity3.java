package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusActivity3 extends AppCompatActivity {

    // 변수들

    String hour, min;
    String start,arrival;
    // 출발지
    TextView start_txt;
    TextView arrival_txt;
    TextView time_txt;

    // 데이터 들
    XMLParser parser = null;
    String [] MJUSTATION_TIMETABLE;
    String [] MJUSTATION_TIMEREQUIRE;
    String [] MJUSTATION_STATIONS;
    String [] CITY_TIMETABLE;
    String [] CITY_TIMEREQUIRE;
    String [] CITY_STATIONS;

    //버스 출발 시간
    String[] startTimes;
    //버스가 도착할 시간
    DateFormat arrivalTime;
    //입력받은 시간
    DateFormat targetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus3);

        //데이터 설정
        try {
            parser = new XMLParser(getResources().openRawResource(R.raw.businfo));
            MJUSTATION_STATIONS = parser.getElementByName("BUS_MJUSTATION_STATIONS");
            MJUSTATION_TIMEREQUIRE = parser.getElementByName("BUS_MJUSTATION_TIMEREQUIRE");
            MJUSTATION_TIMETABLE = parser.getElementByName("BUS_MJUSTATION_TIMETABLE");
            CITY_STATIONS = parser.getElementByName("BUS_CITY_STATIONS");
            CITY_TIMEREQUIRE = parser.getElementByName("BUS_CITY_TIMEREQUIRE");
            CITY_TIMETABLE = parser.getElementByName("BUS_CITY_TIMETABLE");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        
        hour = intent.getStringExtra("hour");
        min = intent.getStringExtra("min");
        start = intent.getStringExtra("start");
        arrival = intent.getStringExtra("arrival");
        targetTime = new DateFormat(hour + ":" + min);

        //타겟시간에 대한 가까운 셔틀 버스찾기 : 1차 알고리즘 작성 (메서드 분리 이전, 기능 별 항목 구현)
        //MJUSTATION과 CITYSTATION 모두 포함한다면: 두 버스 모두 지나가는 정류장 (이 때 시간을 정렬해서 새로운 시간표를 만든다)
        if(Arrays.asList(MJUSTATION_STATIONS).contains(start)&&Arrays.asList(CITY_STATIONS).contains(start)){
            String [] SORTED_INTEGRATED_TIMETABLE = integrateTimeTable();
            startTimes = Search.BinarySearch(targetTime.getTime(), SORTED_INTEGRATED_TIMETABLE);

            //2차 알고리즘 작성: 가까운 셔틀 버스 출발 시간을 이욯하여, start 정류장에 도착할 시간 구하기
            //input: 타겟 시간, 버스 출발 시간(직전 버스, 이후 버스), 정류장, TIME_REQUIRE 배열
            //시작 정류장에서부터, 현재정류장까지 걸리는 시간을 구한다
            //직전 버스 출발 시간에 위에서 구한 시간을 더했을 때, 타겟 시간 후라면 이 시간이 도착예정 시간
            //타겟 시간 전이라면, 이미 버스가 지나간 것이므로, 이후 버스에 대해서 위에서 구한 시간을 더하면 이 시간이 도착예정 시간'

            arrivalTime = compareArrivalTime(start, startTimes);
        }
        //출발지 정류장이 만약 CITYSTATION_STATIONS에만 있다면 : 명지대역버스만 지나가는 정류장
        else if(Arrays.asList(CITY_STATIONS).contains(start)){
            startTimes = Search.BinarySearch(targetTime.getTime(), CITY_TIMETABLE);
            arrivalTime = compareArrivalTime(start, startTimes);
        }
        //출발지 정류장이 만약 MJSTATION_STATIONS에만 있다면: 시내버스만 지나가는 정류장
        else{
            startTimes = Search.BinarySearch(targetTime.getTime(), MJUSTATION_TIMETABLE);
            arrivalTime = compareArrivalTime(start, startTimes);
        }
        
        //테스트 데이터 출력
        Log.d("타겟시간", targetTime.getTime());
        Log.d("내가 탈 정류장", start);
        Log.d("출발시간", Arrays.toString(startTimes));
        Log.d("도착시간", arrivalTime.getTime());

        //데이터 적용
        start_txt = findViewById(R.id.textView6);
        start_txt.setText(start);

        arrival_txt = findViewById(R.id.textView7);
        arrival_txt.setText(arrival);

        time_txt = findViewById(R.id.textView10);
        //남은시간: 도착예정 시간 + (-타겟 시간)
        time_txt.setText(DateFormat.compare(arrivalTime.getTime(), targetTime.getTime()) + "분");

    }
    //시간표 통합 메서드
    //두 시간표를 통합하여 시간별로 sort후, 반환한다.
    public String[] integrateTimeTable(){
        List<String> list1 = new ArrayList<>(Arrays.asList(MJUSTATION_TIMETABLE));
        List<String> list2 = new ArrayList<>(Arrays.asList(CITY_TIMETABLE));
        list1.addAll(list2);
        //시내버스, 명지대역 통합 버스 시간표
        String [] integrateTime = list1.toArray(new String[0]);
        //정렬
        String [] sortedIntegrateTime = QuickSort.sort(integrateTime);

        return sortedIntegrateTime;
    }

    //도착 시간 구하기 메서드
    //INPUT: 도착 정류장, 버스 출발시간
    //도착 정류장이 어떤 TIMETABLE에 속해있는지 판단 한 후, 해당 노선도의 TIMEREQUIRE테이블을 이용해서 도착 정류장에 버스가 도착할 시간을 반환한다.
    public DateFormat getArrivalTime(String startStation, String startTime){

        DateFormat arrivalTime = new DateFormat(startTime);

        int stationIndex = 0;
        //버스가 명지대역 버스라면
        if(Arrays.asList(MJUSTATION_TIMETABLE).contains(startTime)){
            for (int i = 0; i < MJUSTATION_STATIONS.length; i++) {
                if (MJUSTATION_STATIONS[i].equals(startStation)) {
                    stationIndex = i;
                }
            }
            //구해진 stationIndex까지 즉 출발점부터 start 정류장까지 걸리는 시간을 더한다.
            for(int i =0; i<=stationIndex; i++){
                arrivalTime.addTime(Integer.parseInt(MJUSTATION_TIMEREQUIRE[i]));
            }
        }
        //버스가 시내 버스라면
        else {
            for (int i = 0; i < CITY_STATIONS.length; i++) {
                if (CITY_STATIONS[i].equals(start)) {
                    stationIndex = i;
                }
            }
            for(int i =0; i<stationIndex; i++){
                arrivalTime.addTime(Integer.parseInt(CITY_TIMEREQUIRE[i]));
            }
        }
        return arrivalTime;
    }

    //도착 버스 시간 비교 메서드
    //INPUT : 도착 정류장, 2개의 버스출발시간(타겟시간 이전버스, 타겟시간 이후버스)
    //직전 버스, 이후 버스 중 어떤 버스가 먼저 도착할 지 판단한 후 먼저 도착하는 버스의 도착시간을 반환한다.
    public DateFormat compareArrivalTime(String startStation, String [] bustTime){

        DateFormat arrivalTime;

        DateFormat bus1ArrivalTime = getArrivalTime(startStation, bustTime[0]);
        DateFormat bus2ArrivalTime = getArrivalTime(startStation, bustTime[1]);

        //이전에 출발한 버스 시간 > 타겟 시간 : 아직 도착 전이기 때문에 ArrivalTime은 이전에 출발한 버스가 도착할 시간이 된다.
        if(DateFormat.compare(bus1ArrivalTime.getTime(), targetTime.getTime()) > 0){
            arrivalTime = bus1ArrivalTime;
        }
        //이전에 출발한 버스 시간 < 타겟 시간 : ArrivalTime은 이후에 출발한 버스가 도착할 시간이 된다.
        else{
            arrivalTime = bus2ArrivalTime;
        }
        return arrivalTime;
    }
}