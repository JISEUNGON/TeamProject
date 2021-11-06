package com.example.busapp;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONParser {
    public JSONParser() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer parseDuration(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
        String json = streamOfString.collect(Collectors.joining());

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONObject("route")
                                            .getJSONArray("trafast")
                                            .getJSONObject(0)
                                            .getJSONArray("guide");

            int duration = 0;
            for(int i=0; i < jsonArray.length(); i++) {
                duration += jsonArray.getJSONObject(i).getInt("duration");
            }
            return duration;
        } catch (Exception e) {
            Log.d("JSON PARSE ERROR", e.toString());
            e.printStackTrace();
        }
        return -1;
    }
}
