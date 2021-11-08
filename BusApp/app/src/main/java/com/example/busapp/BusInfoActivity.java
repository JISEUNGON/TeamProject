package com.example.busapp;


import android.os.Bundle;
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
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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

        /**
         * 승언님 여기서 작성하시면 될 것 같습니다.  부탁드려요
         * https://1d1cblog.tistory.com/140
         * 저도 요기 참고해서 코드 작성했으니 참고해보세용
         *
         * busSpinner_position
         *      0: X
         *      1: 명지대역
         *      2: 시내
         * weekendSpinner_position:
         *      0: 평일
         *      1: 공휴일
         */

        for(int j=0; j < 5; j++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            for(int i=0; i<2; i++) {
                TextView textView = new TextView(this);
                textView.setText("HELLO WORLD");
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
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
    }
}
