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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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


    /**
     * NaverMap API 사용을 하기 위한 KEY 및 변수입니다.
     */
    private static final String NAVER_SECRETKEY = BuildConfig.NAVER_CLIENT_SECRET;
    private static final String NAVER_CLIENTID = BuildConfig.NAVER_CLIENT_ID;
    private static final String NAVER_endPoint = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
    private static final String NAVER_SRC = "?start=";
    private static final String NAVER_DEST = "&goal=";
    private static final String NAVER_OPTION = "&option=trafast";

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
     * 진입로(명지대 방향)에 도착하는 시내 버스 정보를 API를 통하여 가져옵니다.
     * @return 가장 빨리 도착하는 버스(minute)
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
//                Log.d("DBG", Arrays.toString(result));
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

        if (min == 9999) return null;
        else return min;
    }

    /**
     * TODO 1. 정류장 최적화 제한
     *      2.
     * @param stationInfo 정류장 정보
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Integer[] getRouteInfo(ArrayList<Double[]> stationInfo) {
        ArrayList<Integer> stationTime = new ArrayList<>();
        try {
            for (int i = 0; i < stationInfo.size() - 1; i++) {
//                Log.d("SRC : ", stationInfo.get(i)[1] + "," + stationInfo.get(i)[0] );
//                Log.d("DEST : ", stationInfo.get(i + 1)[1] + "," + stationInfo.get(i + 1)[0]);
                String apiUrl = NAVER_endPoint + NAVER_SRC + stationInfo.get(i)[1] + "," + stationInfo.get(i)[0] + NAVER_DEST + stationInfo.get(i + 1)[1] + "," + stationInfo.get(i + 1)[0] + NAVER_OPTION;
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/xml");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", NAVER_CLIENTID);
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", NAVER_SECRETKEY);

                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    stationTime.add(JSONParser.parseDuration(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    Log.d("ERROR", rd.readLine());
                }
            }
        } catch (Exception e) {
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }
//        Log.d("STATION TIME :", Arrays.toString(stationTime.toArray(new Integer[0])));
        return  stationTime.stream().map(x -> (int) Math.ceil((x / 1000) / 60)).toArray(Integer[]::new);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer[] getStationRouteInfo() {
       return getRouteInfo((ArrayList<Double[]>) MapMarkerManager.getStationRoute());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer[] getCityRouteInfo() {
        return getRouteInfo((ArrayList<Double[]>) MapMarkerManager.getCityRoute());
    }

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
