package com.example.busapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;


public class BusInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private MapMarkerManager mapMarkerManager;
    private MapPolyManager mapPolyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businfo);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.BusMapView);

        mapMarkerManager = new MapMarkerManager(mapView);
        mapPolyManager = new MapPolyManager(mapView);
        mapViewContainer.addView(mapView);

        Spinner busSpinner = findViewById(R.id.busSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.busCat, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSpinner.setAdapter(arrayAdapter);

        busSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("[Executed!]", ".");
       if (i == 0) {
           // 명지대역
           mapMarkerManager.setMarkers(false, true);
           mapPolyManager.setPolyLine(false, true);
       } else {
           mapMarkerManager.setMarkers(true, false);
           mapPolyManager.setPolyLine(true, false);
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mapMarkerManager.setMarkers(false, true);
        mapPolyManager.setPolyLine(false, true);
    }
}
