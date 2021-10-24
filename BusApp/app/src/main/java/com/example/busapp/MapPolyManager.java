package com.example.busapp;

import android.graphics.Color;
import android.util.Log;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.LinkedList;

public class MapPolyManager {
    private LinkedList<Double[]> stationPath;
    private LinkedList<Double[]> cityPath;
    private MapView mapView;
    private MapPolyline mapPolyline;

    public MapPolyManager(MapView mapView) {
        this.mapView = mapView;
        mapPolyline = new MapPolyline();
        mapPolyline.setTag(1000);
        mapPolyline.setLineColor(Color.argb(128, 255, 51, 0));

        stationPath = new LinkedList<>();

        // 1. 버스 정류소 ~ 진입로 정류장
        stationPath.add(new Double[]{37.22426266116359, 127.18708882379035});
        stationPath.add(new Double[]{37.22425400771773, 127.18729647503024});
        stationPath.add(new Double[]{37.22467677145656, 127.18777649304232});
        stationPath.add(new Double[]{37.22545086696076, 127.18799710738335});
        stationPath.add(new Double[]{37.22758220293855, 127.1875234825655});

        // 2. 진입로 정류장 ~ 명지대역
        stationPath.add(new Double[]{37.236137583028516, 127.1891371746645});

        // 3. 명지대역 ~ 진입로 정류장
        stationPath.add(new Double[]{37.238304750869176, 127.18989874207213});
        stationPath.add(new Double[]{37.23869504860537, 127.18611569245844});
        stationPath.add(new Double[]{37.23694096024548, 127.18511268142272});
        stationPath.add(new Double[]{37.236137583028516, 127.1891371746645});

        // 4. 진입로 정류장 ~ 명지대 입구
        stationPath.add(new Double[]{37.22758220293855, 127.1875234825655});
        stationPath.add(new Double[]{37.22545086696076, 127.18799710738335});
        stationPath.add(new Double[]{37.22467677145656, 127.18777649304232});

        // 5. 명지대 입구 ~ 명진당 ~ 제3공학관
        stationPath.add(new Double[]{37.22325681570014, 127.18803207785596});
        stationPath.add(new Double[]{37.222018782706456, 127.18908653666968});
        stationPath.add(new Double[]{37.22116685707866, 127.18749330322773});
        stationPath.add(new Double[]{37.221081051089584, 127.18684677373614});
        stationPath.add(new Double[]{37.22038234154487, 127.18668514136321});
        stationPath.add(new Double[]{37.22011974285906, 127.18645484060292});
        stationPath.add(new Double[]{37.21947552803107, 127.1837952234394});
        stationPath.add(new Double[]{37.219509212602546, 127.1829915220452});


        cityPath = new LinkedList<>();

        // 1. 버스 정류소 ~ 진입로 정류장
        cityPath.add(new Double[]{37.22426266116359, 127.18708882379035});
        cityPath.add(new Double[]{37.22425400771773, 127.18729647503024});
        cityPath.add(new Double[]{37.22467677145656, 127.18777649304232});
        cityPath.add(new Double[]{37.22545086696076, 127.18799710738335});
        cityPath.add(new Double[]{37.22758220293855, 127.1875234825655});
        cityPath.add(new Double[]{37.236137583028516, 127.1891371746645});

        // 2. 진입로 정류장 ~ 동부경찰서
        cityPath.add(new Double[]{37.2347648570182, 127.19544604515174});
        cityPath.add(new Double[]{37.234835353601895, 127.19885071960425});

        // 3. 동부 경찰서 ~ 용인시장
        cityPath.add(new Double[]{37.23489943167298, 127.20343664047257});
        cityPath.add(new Double[]{37.235430174474516, 127.20667763142193});
        cityPath.add(new Double[]{37.23549517509722, 127.208705432844});

        // 4. 용인시장 ~ 중앙공영주차장 앞
        cityPath.add(new Double[]{37.23322070497441, 127.2090417528162});

        // 5. 중앙공영 주차장 앞 ~ 명지대 사거리
        cityPath.add(new Double[]{37.23288608117436, 127.20430544744865});
        cityPath.add(new Double[]{37.232933080076286, 127.19959286096265});
        cityPath.add(new Double[]{37.23474251550567, 127.19546073918873});
        cityPath.add(new Double[]{37.23612893664601, 127.18914449591065});

        // 6. 명지대 사거리 ~ 학교 앞
        cityPath.add(new Double[]{37.22758220293855, 127.1875234825655});
        cityPath.add(new Double[]{37.22545086696076, 127.18799710738335});
        cityPath.add(new Double[]{37.22467677145656, 127.18777649304232});

        cityPath.add(new Double[]{37.22328297276092, 127.18801599794055});
        cityPath.add(new Double[]{37.22253624392127, 127.1863851072646});
        cityPath.add(new Double[]{37.22139990324088, 127.18729025158973});
        cityPath.add(new Double[]{37.221491088401194, 127.18766870241903});
        cityPath.add(new Double[]{37.22140187181845, 127.1878591677128});
        cityPath.add(new Double[]{37.22104946526129, 127.18682841434013});
        cityPath.add(new Double[]{37.2203268036754, 127.1866939682504});
        cityPath.add(new Double[]{37.220098529340156, 127.18639205734287});
        cityPath.add(new Double[]{37.21949630424048, 127.1837871643546});
        cityPath.add(new Double[]{37.219509212602546, 127.1829915220452});
    }

    public void setPolyLine(boolean city, boolean station) {
        // 초기화 코드
        // 둘 중 하나만 쓰면 GC가 수거를 안함... 왜?
        mapPolyline = new MapPolyline();
        mapView.removeAllPolylines();
        if (city) {
            for (Double[] path: cityPath) {
                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(path[0], path[1]));
            }
        } else {
            for (Double[] path: stationPath) {
                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(path[0], path[1]));
            }
        }
        mapView.addPolyline(mapPolyline);
        MapPointBounds mapPointBounds = new MapPointBounds(mapPolyline.getMapPoints());
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 200));
    }
}

