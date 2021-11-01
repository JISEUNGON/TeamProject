package com.example.busapp;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CityBusManager {
    private static final String endPoint = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalItem";
    private static final String sericeKey = "ZJF99uIbDjNnsZBlrbDg%2BDL%2FCyHI2Vc%2BATgI41upeL1%2FGf2jjy8keoY%2FEb6E6CLtokViU7v8bN8tRY0vJ2x3EQ%3D%3D";
//    private static final String sericeKey = "ZJF99uIbDjNnsZBlrbDg+DL/CyHI2Vc+ATgI41upeL1/Gf2jjy8keoY/Eb6E6CLtokViU7v8bN8tRY0vJ2x3EQ==";
    private static final String stationID = "228002023";
    /**
     * 5003번   228000182(노선 ID) 98(정류장 순번)
     * 5001-1번 228000177(노선 ID) 73(정류장 순번)
     * 5000B번  228000174(노선 ID) 110(정류장 순번)
     */
    private static final String[] routeIDs = new String[]{"228000182", "228000177", "228000174"};
    private static final String[] staOrders = new String[]{"98", "73", "110"};

    public static String getClosestCityBus() {
        Log.d("==============","Executed!");
        String bus_5003 = getBusInfo(routeIDs[0], staOrders[0]);
        String bus_5001 = getBusInfo(routeIDs[1], staOrders[1]);
        String bus_5000 = getBusInfo(routeIDs[2], staOrders[2]);

        return Integer.toString(
                    Math.min(Integer.parseInt(bus_5000),
                    Math.min(Integer.parseInt(bus_5001),
                             Integer.parseInt(bus_5003))));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static String getBusInfo(String routeID, String staOrder) {
        String apiUrl = endPoint + "?serviceKey=" + sericeKey + "&stationId=" + stationID + "&routeId=" + routeID + "&staOrder=" + staOrder;
        Log.d("=======================", apiUrl);
        String result = null;
        try {
            // Open the connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000000);
            conn.setReadTimeout(1000000);
            conn.setDoOutput(true);

            int status = conn.getResponseCode();
            Log.d("D/STATUS CODE", String.valueOf(status));
            InputStream error = conn.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(error);
            Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
            String streamToString = streamOfString.collect(Collectors.joining());
            Log.d("DDDD", streamToString);
            InputStream is = conn.getInputStream();
            result = parse(is);


        } catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private static String parse(InputStream xmlStream) {
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(new InputStreamReader(xmlStream)); // parser가 stream을 읽도록

            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag != null && tag.equals("predictTime1")) {
                    parser.next();
                    return parser.getText();
                }
                event = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return "9999";
    }
}
