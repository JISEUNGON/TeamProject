package com.example.busapp;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

public class BusManager {
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
            MJUSTATION_STATIONS = parser.getElementByName("BUS_MJUSTATION_STATIONS");
            MJUSTATION_TIMEREQUIRE = parser.getElementByName("BUS_MJUSTATION_TIMEREQUIRE");
            MJUSTATION_TIMETABLE = parser.getElementByName("BUS_MJUSTATION_TIMETABLE");
            CITY_STATIONS = parser.getElementByName("BUS_CITY_STATIONS");
            CITY_TIMEREQUIRE = parser.getElementByName("BUS_CITY_TIMEREQUIRE");
            CITY_TIMETABLE = parser.getElementByName("BUS_CITY_TIMETABLE");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

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
