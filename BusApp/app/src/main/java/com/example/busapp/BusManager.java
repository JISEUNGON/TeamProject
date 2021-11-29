package com.example.busapp;

import android.os.Build;
import android.util.Log;

import org.json.JSONObject;
import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusManager {
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
     * 명지대역 셔틀버스
     * * 실시간으로 값을 받아옵니다
     *
     * @return [x, x, x, x, x,] int[]
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] getStationRouteInfo() {
        return getRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/OjJo45tmXK/json");
    }


    /**
     * 시내방향 셔틀버스
     * * 실시간으로 값을 받아옵니다.
     *
     * @return [x, x, x, x, x,] int[]
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] getCityRouteInfo() {
        return getRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/9KC9LrEB87/json");
    }

    /**
     * 명지대역 셔틀버스
     * * 과거 데이터에 의존하여 값을 받습니다
     *
     * @param time
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] predictShuttleTime(String time) {
        int[] routeInfo = getPastRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/14gvgz0L2D/json", time);
        if (routeInfo == null) return new int[]{3, 1, 3, 4, 1, 1};
        else return routeInfo;
    }

    /**
     * 시내 버스 (진입로)
     * * 과거 데이터에 의존하여 값을 받습니다
     *
     * @param time
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] predictBusTime(String time) {
        int[] routeInfo = getPastRouteInfo("https://yax35ivans.apigw.ntruss.com/mba/v1/UKRrahCJdv/json", time);
        return routeInfo;
    }

    /**
     * 진입로(명지대 방향)에 도착하는 시내 버스 정보를 API를 통하여 가져옵니다.
     *
     * @return 가장 빨리 도착하는 버스(minute)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getClosestCityBus() {
        try {
            URL url = new URL("https://yax35ivans.apigw.ntruss.com/mba/v1/TLAVUb32yo/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());
                if (streamOfString.equals("")) return 9999;
                else return Integer.parseInt(streamToString);
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                Log.d("ERROR", rd.readLine());
            }
        } catch (Exception e) {
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }
        return 9999;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int[] getPastRouteInfo(String endpoint, String time) {
        String day = toDayString(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");

            conn.setDoOutput(true);
            String jsonString = "{ \"time\": \"" + time + "\", \"day\": \"" + day + "\"}";
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
                String streamToString = streamOfString.collect(Collectors.joining());

                return Arrays.stream(
                        new JSONObject(streamToString).getString("body").split(",")
                ).mapToInt(Integer::parseInt)
                        .toArray();
            } else {
                Log.d("POST FUTURE ERROR", String.valueOf(conn.getResponseCode()));
                Log.d("POST FUTURE ERROR", String.valueOf(conn.getURL()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int[] getRouteInfo(String endPointUrl) {
        try {
            URL url = new URL(endPointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
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

    private static String toDayString(int idx) {
        return new String[]{"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"}[idx];
    }


    public String[] BUS_MJUSTATION_STATIONS() {
        if (MJUSTATION_STATIONS == null) {
            MJUSTATION_STATIONS = parser.getElementByName("BUS_MJUSTATION_STATIONS", true);
        }
        return MJUSTATION_STATIONS;
    }

    public String[] BUS_MJUSTATION_TIMEREQUIRE() {
        if (MJUSTATION_TIMEREQUIRE == null) {
            MJUSTATION_TIMEREQUIRE = parser.getElementByName("BUS_MJUSTATION_TIMEREQUIRE", true);
        }
        return MJUSTATION_TIMEREQUIRE;
    }

    public String[] BUS_MJUSTATION_TIMETABLE() {
        if (MJUSTATION_TIMETABLE == null) {
            MJUSTATION_TIMETABLE = parser.getElementByName("BUS_MJUSTATION_TIMETABLE", true);
        }
        return MJUSTATION_TIMETABLE;
    }

    public String[] BUS_CITY_STATIONS() {
        if (CITY_STATIONS == null) {
            CITY_STATIONS = parser.getElementByName("BUS_CITY_STATIONS", true);
        }
        return CITY_STATIONS;
    }

    public String[] BUS_CITY_TIMEREQUIRE() {
        if (CITY_TIMEREQUIRE == null) {
            CITY_TIMEREQUIRE = parser.getElementByName("BUS_CITY_TIMEREQUIRE", true);
        }
        return CITY_TIMEREQUIRE;
    }

    public String[] BUS_CITY_TIMETABLE() {
        if (CITY_TIMETABLE == null) {
            CITY_TIMETABLE = parser.getElementByName("BUS_CITY_TIMETABLE", true);
        }
        return CITY_TIMETABLE;
    }
}