package com.example.busapp;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.InputStream;
import java.util.Arrays;

public class BusInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private MapView mapView;
    private MapMarkerManager mapMarkerManager;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private NaverMap naverMap;
    private MapPolyManager mapPolyManager;

    // 스피너가 선택한 값 저장
    private Integer busSpinner_position = -1;
    private Integer weekendSpinner_position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businfo);

        mapView = findViewById(R.id.bus_map_view);
        mapView.getMapAsync(this);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(0.4f);

        Spinner busSpinner = findViewById(R.id.busSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.busCat, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSpinner.setAdapter(arrayAdapter);
        busSpinner.setOnItemSelectedListener(this);

        Spinner weekendSpinner = findViewById(R.id.weekendSpinner);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.weekend, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekendSpinner.setAdapter(arrayAdapter);
        weekendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weekendSpinner_position = position;
                createBusTimeTable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weekendSpinner_position = 0;
            }
        });
        createBusTimeTable();
    }

    private void createBusTimeTable() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.bus_timeTable);
        tableLayout.removeAllViews();
        /**
         * busSpinner_position
         *      0: X
         *      1: 명지대역
         *      2: 시내
         * weekendSpinner_position:
         *      0: 평일
         *      1: 공휴일
         */
        try {
            XMLParser parser = new XMLParser(getResources().openRawResource(R.raw.businfo));
            String[] timeTable = null;
            if(busSpinner_position >= 1 && weekendSpinner_position >= 0) {
                if(busSpinner_position == 1 && weekendSpinner_position == 0) {
                    // 명지대역, 평일
                    timeTable = parser.getElementByName("BUS_MJUSTATION_TIMETABLE", true);
                } else if(busSpinner_position == 1 && weekendSpinner_position == 1) {
                    // 명지대역, 공휴일
                    timeTable = null;
                } else if(busSpinner_position == 2 && weekendSpinner_position == 0) {
                    // 시내, 평일
                    timeTable = parser.getElementByName("BUS_CITY_TIMETABLE", true);
                } else if(busSpinner_position == 2 && weekendSpinner_position == 1) {
                    // 시내, 공휴일
                    timeTable = parser.getElementByName("BUS_CITY_TIMETABLE_WEEKEND", true);
                }
            }
            if(timeTable == null) return;
            for(String time: timeTable) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                for(int i=0; i<2; i++) {
                    TextView textView = new TextView(this);
                    if(i == 0) {
                        textView.setText(time);
                    } else {
                        DateFormat dateFormat = new DateFormat(time);
                        dateFormat.addTime(25);
                        textView.setText(dateFormat.getTime());
                    }

                    textView.setGravity(Gravity.CENTER);
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        busSpinner_position = i;
        CameraUpdate cameraUpdate = null;
        switch(i) {
            case 0: return;
            case 1: // 명지대역
                mapMarkerManager.setMarkers(false, true);
                mapPolyManager.setPolyLine(false, true);
                cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        new LatLng(37.2311084, 127.18953090000002),13)
                        .animate(CameraAnimation.Fly, 3000);
                naverMap.moveCamera(cameraUpdate);
                break;
            case 2: // 시내
                mapMarkerManager.setMarkers(true, false);
                mapPolyManager.setPolyLine(true, false);
                cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        new LatLng(37.23031233626919, 127.1941142719217),13)
                        .animate(CameraAnimation.Fly, 3000);
                naverMap.moveCamera(cameraUpdate);
                break;
            default:
        }
        createBusTimeTable();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        busSpinner_position = 0;
        mapMarkerManager.setMarkers(false, true);
        mapPolyManager.setPolyLine(false, true);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
        mapMarkerManager = new MapMarkerManager(naverMap);
        mapPolyManager = new MapPolyManager(naverMap);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        MapMarkerManager mapMarkerManager = new MapMarkerManager(naverMap);
        MapPolyManager mapPolyManager = new MapPolyManager(naverMap);

        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                new LatLng(37.233972549267705, 127.18874893910944),15)
                .animate(CameraAnimation.Fly, 3000);
        naverMap.moveCamera(cameraUpdate);
        naverMap.setLocationSource(new FusedLocationSource(this, 1000));
    }
}
