package com.example.busapp;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Kakao Map API에 사용될 정류장 별 Marker 설정을 도와주는 클래스 입니다.
 * 정류장과 그 위치(위도, 경도)는 하드 코딩 되어 있으며, 정류장 변화시 애플리케이션 업데이트를 통하여 변경합니다.
 */
public class MapMarkerManager {
    private HashMap<String, Double[]> stationInfo;
    private MapView mapView;
    private final String[] stations = new String[]{"명지대", "상공회의소", "진입로", "명지대역", "진입로(명지대방향)", "이마트", "명진당", "제3공학관", "동부경찰서", "용인시장", "중앙공영주차장", "제1공학관"};
    private final Double[][] positions = new Double[][]{
            new Double[]{37.22426266116359 , 127.18708882379035},
            new Double[]{37.23057289820615 , 127.18817054455151},
            new Double[]{37.233972549267705, 127.18874893910944},
            new Double[]{37.238375611745454, 127.18972811341291},
            new Double[]{37.234053441164306, 127.18866744576944},
            new Double[]{37.23036920601031 , 127.18799722805205},
            new Double[]{37.22218358841614, 127.18895343450612},
            new Double[]{37.219509212602546, 127.1829915220452},
            new Double[]{37.23475516860965 , 127.19817660622552},
            new Double[]{37.235430174474516, 127.20667763142193},
            new Double[]{37.23391585619981 , 127.20892718244508},
            new Double[]{37.22271140883418, 127.18678412115244},
    };

    public MapMarkerManager(MapView mapView) {
        stationInfo = new HashMap<>();
        for(int i=0; i<stations.length; i++) stationInfo.put(stations[i], positions[i]);
        this.mapView = mapView;
    }

    /**
     * 마커를 표시합니다.
     * @param city 용인 시내 노선의 정류장을 Mark
     * @param station 명지대역 노선의 정류장을 Mark
     */
    public void setMarkers(boolean city, boolean station) {
        mapView.removeAllPOIItems();

        String[] targetStations = new String[0];
        if (city && station) {
            targetStations = stations;
        } else if(city && !station) {
            targetStations = new String[]{"명지대", "상공회의소", "진입로", "진입로(명지대방향)", "이마트", "동부경찰서", "용인시장", "중앙공영주차장", "제1공학관", "제3공학관"};
        } else if (!city && station) {
            targetStations = new String[]{"명지대", "상공회의소", "진입로", "명지대역", "진입로(명지대방향)", "이마트", "명진당", "제3공학관"};
        } else {
            return;
        }
        for (String cur_station: targetStations){
            MapPOIItem marker = new MapPOIItem();
            Double[] position = stationInfo.get(cur_station);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(position[0], position[1]));
            marker.setItemName(cur_station);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);

        }
    }

    /**
     * 정류장의 위치(위도, 경도)를 되돌려 줍니다.
     * @param station 찾으려는 정류장
     * @return (위도, 경도)
     */
    public Double[] getPosition(String station) {
        return stationInfo.get(station);
    }
}
