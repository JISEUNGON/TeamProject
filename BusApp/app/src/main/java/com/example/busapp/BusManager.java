package com.example.busapp;

import android.os.Build;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private XMLParser parser = null;
    private String[] MJUSTATION_STATIONS = null;
    private String[] MJUSTATION_TIMEREQUIRE = null;
    private String[] MJUSTATION_TIMETABLE = null;
    private String[] CITY_STATIONS = null;
    private String[] CITY_TIMEREQUIRE = null;
    private String[] CITY_TIMETABLE = null;

    public BusManager(InputStream stream) {
        try {
            parser = new XMLParser(stream);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] getRouteInfo(String endPointUrl) {
        try {
            URL url = new URL(endPointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());

                return Arrays.stream(
                             new JSONObject(streamToString).getString("body").split(",")
                            ).mapToInt(Integer::parseInt)
                             .toArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] getStationRouteInfo() {
        return getRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/OjJo45tmXK/json");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] getCityRouteInfo() {
        return getRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/9KC9LrEB87/json");
    }

    public String[] BUS_MJUSTATION_STATIONS() {
        if(MJUSTATION_STATIONS == null) {
            MJUSTATION_STATIONS = parser.getElementByName("BUS_MJUSTATION_STATIONS", true);
        }
        return MJUSTATION_STATIONS;
    }

    public String[] BUS_MJUSTATION_TIMEREQUIRE() {
        if(MJUSTATION_TIMEREQUIRE == null) {
            MJUSTATION_TIMEREQUIRE = parser.getElementByName("BUS_MJUSTATION_TIMEREQUIRE", true);
        }
        return MJUSTATION_TIMEREQUIRE;
    }

    public String[] BUS_MJUSTATION_TIMETABLE() {
        if(MJUSTATION_TIMETABLE == null) {
            MJUSTATION_TIMETABLE = parser.getElementByName("BUS_MJUSTATION_TIMETABLE", true);
        }
        return MJUSTATION_TIMETABLE;
    }

    public String[] BUS_CITY_STATIONS() {
        if(CITY_STATIONS == null) {
            CITY_STATIONS = parser.getElementByName("BUS_CITY_STATIONS", true);
        }
        return CITY_STATIONS;
    }

    public String[] BUS_CITY_TIMEREQUIRE() {
        if(CITY_TIMEREQUIRE == null) {
            CITY_TIMEREQUIRE = parser.getElementByName("BUS_CITY_TIMEREQUIRE", true);
        }
        return CITY_TIMEREQUIRE;
    }

    public String[] BUS_CITY_TIMETABLE() {
        if(CITY_TIMETABLE == null) {
            CITY_TIMETABLE = parser.getElementByName("BUS_CITY_TIMETABLE", true);
        }
        return CITY_TIMETABLE;
    }
}
