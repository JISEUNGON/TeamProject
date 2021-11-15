package com.example.busapp;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Kakao Map API에 사용될 정류장 별 Marker 설정을 도와주는 클래스 입니다.
 * 정류장과 그 위치(위도, 경도)는 하드 코딩 되어 있으며, 정류장 변화시 애플리케이션 업데이트를 통하여 변경합니다.
 */
public class MapMarkerManager {
    private final static HashMap<String, Double[]> stationInfo = new HashMap<String, Double[]>() {{
            put("명지대",
                    new Double[]{37.224283500000006, 127.18728609999998});
            put("상공회의소",
                    new Double[]{37.230680400000004, 127.1882456});
            put("진입로",
                    new Double[]{37.23399210000001,  127.18882909999999});
            put("명지대역",
                    new Double[]{37.238513300000015, 127.18960559999998});
            put("진입로(명지대방향)",
                    new Double[]{37.233999900000015, 127.18861349999999});
            put("이마트",
                    new Double[]{37.23036920601031 , 127.18799722805205});
            put("명진당",
                    new Double[]{37.22218358841614, 127.18895343450612});
            put("제3공학관",
                    new Double[]{37.219509212602546, 127.1829915220452});
            put("동부경찰서",
                    new Double[]{37.23475516860965 , 127.19817660622552});
            put("용인시장",
                    new  Double[]{37.235430174474516, 127.20667763142193});
            put("중앙공영주차장",
                    new Double[]{37.23391585619981 , 127.20892718244508});
            put("제1공학관",
                    new Double[]{37.22271140883418, 127.18678412115244});
    }};
    private NaverMap naverMap;
    private ArrayList<Marker> markerArrayList;

    public MapMarkerManager(NaverMap naverMap) {
        this.markerArrayList = new ArrayList<>();
        this.naverMap = naverMap;
    }

    /**
     * 마커를 표시합니다.
     * @param city 용인 시내 노선의 정류장을 Mark
     * @param station 명지대역 노선의 정류장을 Mark
     */
    public void setMarkers(boolean city, boolean station) {
        for(Marker marker: markerArrayList) marker.setMap(null);
        markerArrayList = new ArrayList<>();

        String[] targetStations = new String[0];
        if(city && !station) {
            targetStations = new String[]{"명지대", "상공회의소", "진입로", "진입로(명지대방향)", "이마트", "동부경찰서", "용인시장", "중앙공영주차장", "제1공학관", "제3공학관"};
        } else if (!city && station) {
            targetStations = new String[]{"명지대", "상공회의소", "진입로", "명지대역", "진입로(명지대방향)", "이마트", "명진당", "제3공학관"};
        } else {
            return;
        }
        for(String _station: targetStations) {
            Marker marker = new Marker();
            Double[] position = stationInfo.get(_station);
            marker.setPosition(new LatLng(position[0], position[1]));
            marker.setCaptionText(_station);
            marker.setMap(naverMap);
            markerArrayList.add(marker);
        }
    }

    /**
     * 정류장의 위치(위도, 경도)를 되돌려 줍니다.
     * @param station 찾으려는 정류장
     * @return (위도, 경도)
     */
    public static LatLng getPosition(String station) {
        Double[] pos = stationInfo.get(station);
        return new LatLng(pos[0], pos[1]);
    }

    /**
     * 시내 방향 버스 정보 제공
     * @return 정류장의 위도, 경도 set
     */
    public static List<Double[]> getCityRoute() {
        String[] stations = new String[]{"명지대", "상공회의소", "진입로", "진입로(명지대방향)", "이마트", "동부경찰서", "용인시장", "중앙공영주차장", "제1공학관", "제3공학관"};
        ArrayList<Double[]> info = new ArrayList<>();
        for(String station: stations) {
            info.add(stationInfo.get(station));
        }

        return info;
    }

    /**
     * 명지대역 버스 정보 제공
     * @return
     */
    public static List<Double[]> getStationRoute() {
        String[] stations =  new String[]{"명지대", "상공회의소", "진입로", "명지대역", "진입로(명지대방향)", "이마트", "명진당", "제3공학관"};
        ArrayList<Double[]> info = new ArrayList<>();
        for(String station: stations) {
            info.add(stationInfo.get(station));
        }

        return info;
    }

    public static String[] getStationInfo() {
        return new String[]{"명지대", "상공회의소", "진입로", "명지대역", "진입로(명지대방향)", "이마트", "명진당", "제3공학관"};
    }

    public static String[] getCityInfo() {
        return new String[]{"명지대", "상공회의소", "진입로", "동부경찰서", "용인시장", "중앙공영주차장", "진입로(명지대방향)", "이마트", "제1공학관", "제3공학관"};
    }

    public static HashMap<String, Double[]> getAllStationInfo() {
        return stationInfo;
    }
}
