package com.example.busapp;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CityBusManager {
    private static final String endPoint = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";
    private static final String sericeKey = "ZJF99uIbDjNnsZBlrbDg%2BDL%2FCyHI2Vc%2BATgI41upeL1%2FGf2jjy8keoY%2FEb6E6CLtokViU7v8bN8tRY0vJ2x3EQ%3D%3D";
    private static final String stationID = "228002023";

    /**
     * 5003번   228000182(노선 ID) 98(정류장 순번)
     * 5001-1번 228000177(노선 ID) 73(정류장 순번)
     * 5000B번  228000174(노선 ID) 110(정류장 순번)
     */
    private static final String[] routeIDs = new String[]{"228000182", "228000177", "228000174"};
    private static final String[] staOrders = new String[]{"98", "73", "110"};

    /**
     * @return 정류장에 도착하는 시내 버스 중 가장 작은 값
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer getClosestCityBus() {
        Integer[] result = null;

        try {
            String apiUrl = endPoint + "?serviceKey=" + sericeKey + "&stationId=" + stationID;

            // Open the connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                result = parse(conn.getInputStream());
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
        } catch (Exception e) {
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        int min = 9999;
        for (int time: result) {
            if (time < min) min = time;
        }

        return min;
    }


    private static Integer[] parse(InputStream xmlStream) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Integer requiredTime = null;
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();

            parser.setInput(new InputStreamReader(xmlStream)); // parser가 stream을 읽도록
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag != null) {
                }
                if (tag != null && tag.equals("predictTime1")) {
                    requiredTime = Integer.parseInt(parser.nextText());
                } else if (tag != null && tag.equals("routeId")) {
                    String routeId = parser.nextText();
                    for(String id: routeIDs) {
                        if(id.equals(routeId)) {
                            result.add(requiredTime);
                        }
                    }
                }
                event = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return result.toArray(new Integer[0]);
    }
}
