package com.example.busapp;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class BusInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private MapView mapView;
    private MapMarkerManager mapMarkerManager;
    private MapPolyManager mapPolyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businfo);

        MapView mapView = findViewById(R.id.bus_map_view);
//        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Spinner busSpinner = findViewById(R.id.busSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.busCat, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSpinner.setAdapter(arrayAdapter);

        busSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i) {
            case 0: return;
            case 1:
                mapMarkerManager.setMarkers(false, true);
                mapPolyManager.setPolyLine(false, true);
                break;
            case 2:
                mapMarkerManager.setMarkers(true, false);
                mapPolyManager.setPolyLine(true, false);
                break;
            default:
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mapMarkerManager.setMarkers(false, true);
        mapPolyManager.setPolyLine(false, true);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
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
