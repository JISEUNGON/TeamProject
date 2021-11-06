package com.example.busapp;

import android.os.Build;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * TODO 
 *   1. CityBusManager 통합
 *   2. 현 클래스에서 빨강버스, 셔틀버스를 모두 계산하는 클래스로
 */
public class BusManager {
    /**
     * 시내버스(빨강버스) API 연동을 위한 변수입니다.
     * 5003번   228000182(노선 ID) 98(정류장 순번)
     * 5001-1번 228000177(노선 ID) 73(정류장 순번)
     * 5000B번  228000174(노선 ID) 110(정류장 순번)
     */
    private static final String CityBus_endPoint = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";
    private static final String CityBus_serviceKey = BuildConfig.DATAS_CLIENT_SECRET;
    private static final String CityBus_stationID = "228002023";
    private static final String[] routeIDs = new String[]{"228000182", "228000177", "228000174"};


    private String[] MJUSTATION_STATIONS;
    private String[] MJUSTATION_TIMEREQUIRE;
    private String[] MJUSTATION_TIMETABLE;
    private String[] CITY_STATIONS;
    private String[] CITY_TIMEREQUIRE;
    private String[] CITY_TIMETABLE;

    public BusManager(InputStream stream) {
        XMLParser parser = null;
        try {
            parser = new XMLParser(stream);
            MJUSTATION_STATIONS = parser.getElementByName("BUS_MJUSTATION_STATIONS", true);
            MJUSTATION_TIMEREQUIRE = parser.getElementByName("BUS_MJUSTATION_TIMEREQUIRE", true);
            MJUSTATION_TIMETABLE = parser.getElementByName("BUS_MJUSTATION_TIMETABLE", true);
            CITY_STATIONS = parser.getElementByName("BUS_CITY_STATIONS", true);
            CITY_TIMEREQUIRE = parser.getElementByName("BUS_CITY_TIMEREQUIRE", true);
            CITY_TIMETABLE = parser.getElementByName("BUS_CITY_TIMETABLE", true);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return 정류장에 도착하는 시내 버스 중 가장 작은 값
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer getClosestCityBus() {
        String[] result = null;

        try {
            String apiUrl = CityBus_endPoint + "?serviceKey=" + CityBus_serviceKey + "&stationId=" + CityBus_stationID;

            // Open the connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                XMLParser parser = new XMLParser(conn.getInputStream());
                result = parser.getElementByName("predictTime1", false);
                Log.d("DBG", Arrays.toString(result));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                Log.d("ERROR", rd.readLine());
            }
        } catch (Exception e) {
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        int min = 9999;
        for (String _time: result) {
            int time = Integer.parseInt(_time);
            if (time < min) min = time;
        }

        return min;
    }
//
//    /**
//     * 어떤 정류장이 먼저 앞서 있는지 비교합니다.
//     * @param src 원본
//     * @param dest 비교할 대상
//     * @param busCat 버스 노선
//     * @return src가 dest보다 앞서 있는 경우 > 0
//     *         src가 dest보다 뒤에 있는 경우 < 0
//     */
//    public int compaeStationOrder(String src, String dest, String busCat) {
//        String[] targetStation;
//        switch (busCat) {
//            case "MJUSTATION_STATIONS":
//                targetStation = MJUSTATION_STATIONS; break;
//            case "CITY_STATIONS":
//                targetStation = CITY_STATIONS; break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + busCat);
//        }
//        for(String station: targetStation) {
//            if(station.equals(src)) return 1;
//            if(station.equals(dest)) return -1;
//        }
//        return 0;
//    }

    public String[] BUS_MJUSTATION_STATIONS() {
        return MJUSTATION_STATIONS;
    }

    public String[] BUS_MJUSTATION_TIMEREQUIRE() {
        return MJUSTATION_TIMEREQUIRE;
    }

    public String[] BUS_MJUSTATION_TIMETABLE() {
        return MJUSTATION_TIMETABLE;
    }

    public String[] BUS_CITY_STATIONS() {
        return CITY_STATIONS;
    }

    public String[] BUS_CITY_TIMEREQUIRE() {
        return CITY_TIMEREQUIRE;
    }

    public String[] BUS_CITY_TIMETABLE() {
        return CITY_TIMETABLE;
    }
}
