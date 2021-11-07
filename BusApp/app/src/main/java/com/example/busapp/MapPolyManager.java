package com.example.busapp;

import android.graphics.Color;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.LinkedList;

public class MapPolyManager {
    private LinkedList<LatLng> stationPath;
    private LinkedList<LatLng> cityPath;
    private NaverMap naverMap;
    private PathOverlay path;

    public MapPolyManager(NaverMap naverMap) {
        this.naverMap = naverMap;
        path = new PathOverlay();

        stationPath = new LinkedList<>();

        // 1. 버스 정류소 ~ 상공 회의소
        stationPath.add(new LatLng(37.22426266116359, 127.18708882379035));
        stationPath.add(new LatLng(37.22425400771773, 127.18729647503024));
        stationPath.add(new LatLng(37.22471270000002, 127.1879012));
        stationPath.add(new LatLng(37.22546689999999, 127.18810780000003));
        stationPath.add(new LatLng(37.22714370000001, 127.18769519999998));
        stationPath.add(new LatLng(37.22896940000001, 127.18793339999999));
        stationPath.add(new LatLng(37.23067699999998, 127.18828089999997));

        // 2. 상공 회의소 ~ 명지대 진입로 정류장
        stationPath.add(new LatLng(37.23399210000001,  127.18882909999999));

        // 3. 진입로 정류장 ~ 명지대역
        stationPath.add(new LatLng(37.23474450000002, 127.188946));
        stationPath.add(new LatLng(37.235082300000016, 127.18898490000005));
        stationPath.add(new LatLng(37.23588290000001, 127.18918769999998));
        stationPath.add(new LatLng(37.23837380000001, 127.18997559999994));
        stationPath.add(new LatLng(37.238513300000015, 127.18960559999998));

        // 4. 명지대역 ~ 진입로 정류장
        stationPath.add(new LatLng(37.238609700000005, 127.1888485));
        stationPath.add(new LatLng(37.23881399999999, 127.18613739999998));
        stationPath.add(new LatLng(37.23687370000001, 127.1850938));
        stationPath.add(new LatLng(37.236056800000014, 127.18909959999999));
        stationPath.add(new LatLng(37.2352001, 127.18881200000001));
        stationPath.add(new LatLng(37.23396799999999, 127.18863040000002));

        // 4. 진입로 정류장 ~ 이마트?
        stationPath.add(new LatLng(37.232280100000004, 127.18837540000003));
        stationPath.add(new LatLng(37.23036920601031 , 127.18799722805205));
        stationPath.add(new LatLng(37.22764440000002, 127.18748700000002));

        // 5. 명지대 입구 ~ 명진당 ~ 제3공학관
        stationPath.add(new LatLng(37.22712330000002, 127.18750610000004));
        stationPath.add(new LatLng(37.22557719999999, 127.18792940000002));
        stationPath.add(new LatLng(37.22479979999998, 127.18771650000002));
        stationPath.add(new LatLng(37.22324490000001, 127.18810420000001));

        stationPath.add(new LatLng(37.222018782706456, 127.18908653666968));
        stationPath.add(new LatLng(37.22116685707866, 127.18749330322773));
        stationPath.add(new LatLng(37.221081051089584, 127.18684677373614));
        stationPath.add(new LatLng(37.22038234154487, 127.18668514136321));
        stationPath.add(new LatLng(37.22011974285906, 127.18645484060292));
        stationPath.add(new LatLng(37.21947552803107, 127.1837952234394));
        stationPath.add(new LatLng(37.219509212602546, 127.1829915220452));


        cityPath = new LinkedList<>();

        cityPath.add(new LatLng(37.22426266116359, 127.18708882379035));
        cityPath.add(new LatLng(37.22425400771773, 127.18729647503024));
        cityPath.add(new LatLng(37.22471270000002, 127.1879012));
        cityPath.add(new LatLng(37.22546689999999, 127.18810780000003));
        cityPath.add(new LatLng(37.22714370000001, 127.18769519999998));
        cityPath.add(new LatLng(37.22896940000001, 127.18793339999999));
        cityPath.add(new LatLng(37.23067699999998, 127.18828089999997));

        // 2. 상공 회의소 ~ 명지대 진입로 정류장
        cityPath.add(new LatLng(37.23399210000001,  127.18882909999999));

        // 2. 진입로 정류장 ~ 동부경찰서
        cityPath.add(new LatLng(37.235088999999995, 127.1889761));
        cityPath.add(new LatLng(37.23599719999999, 127.189232));
        cityPath.add(new LatLng(37.2347648570182, 127.19544604515174));
        cityPath.add(new LatLng(37.234835353601895, 127.19885071960425));

        // 3. 동부 경찰서 ~ 용인시장
        cityPath.add(new LatLng(37.23489943167298, 127.20343664047257));
        cityPath.add(new LatLng(37.235430174474516, 127.20667763142193));
        cityPath.add(new LatLng(37.23549517509722, 127.208705432844));

        // 4. 용인시장 ~ 중앙공영주차장 앞
        cityPath.add(new LatLng(37.23322070497441, 127.2090417528162));

        // 5. 중앙공영 주차장 앞 ~ 명지대 진입로
        cityPath.add(new LatLng(37.23325700000001, 127.2089643));
        cityPath.add(new LatLng(37.23300330000001, 127.20429910000001));
        cityPath.add(new LatLng(37.23300879999999, 127.19993819999998));
        cityPath.add(new LatLng(37.2350996, 127.19473199999999));
        cityPath.add(new LatLng(37.2362121, 127.18915839999994));
        cityPath.add(new LatLng(37.23521510000001, 127.18885570000008));
        cityPath.add(new LatLng(37.233999900000015, 127.18861349999999));

        // 4. 진입로 정류장 ~ 이마트?
        cityPath.add(new LatLng(37.232280100000004, 127.18837540000003));
        cityPath.add(new LatLng(37.23036920601031 , 127.18799722805205));
        cityPath.add(new LatLng(37.22764440000002, 127.18748700000002));

        // 6. 명지대 사거리 ~ 학교 앞
        cityPath.add(new LatLng(37.22758220293855, 127.1875234825655));
        cityPath.add(new LatLng(37.22545086696076, 127.18799710738335));
        cityPath.add(new LatLng(37.22467677145656, 127.18777649304232));

        cityPath.add(new LatLng(37.22328297276092, 127.18801599794055));
        cityPath.add(new LatLng(37.22253624392127, 127.1863851072646));
        cityPath.add(new LatLng(37.22139990324088, 127.18729025158973));
        cityPath.add(new LatLng(37.221491088401194, 127.18766870241903));
        cityPath.add(new LatLng(37.22140187181845, 127.1878591677128));
        cityPath.add(new LatLng(37.22104946526129, 127.18682841434013));
        cityPath.add(new LatLng(37.2203268036754, 127.1866939682504));
        cityPath.add(new LatLng(37.220098529340156, 127.18639205734287));
        cityPath.add(new LatLng(37.21949630424048, 127.1837871643546));
        cityPath.add(new LatLng(37.219509212602546, 127.1829915220452));
    }

    public void setPolyLine(boolean city, boolean station) {
        path.setMap(null);
        if (city && !station) {
            path.setCoords(cityPath);
        } else {
            path.setCoords(stationPath);
        }
        path.setWidth(15);
        path.setColor(Color.GREEN);
        path.setMap(naverMap);
    }
}

