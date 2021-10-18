

public class GetBusTime {

    //타겟 시간과 가장 가까운 버스 출발 시간 구하기
    public DateFormat getTargetBusTime(StationData stationData, DateFormat targetTime) {

        int index = 0;
        DateFormat[] startTime = stationData.getStartTimes();
        String[] station = stationData.getStationNames();
        while (startTime[index].getTotalMin() < targetTime.getTotalMin() && index < startTime.length - 1) {
            index++;
        }
        return startTime[index];
    }

    // 타겟시간에서 가장 가까운 출발대의 버스가
    // 파라메터로 주어진 정류장에 도착할 예정 시간을 구한다.
    public DateFormat getArrivalTime(StationData stationData,DateFormat targetTime, String stationName) {

        String[] stationNames = stationData.getStationNames();
        int[] stationTimes = stationData.getStationTimes();
        int stationIndex = 0;
        DateFormat arrivalTime;


        //매칭되는 정류장의 이름의 index를 구한다.
        for (int i = 0; i < stationNames.length; i++) {
            if (stationNames[i].equals(stationName)) {
                stationIndex = i;
            }
        }

        //도착 시간 = 버스 출발 시간 + 현재 정류장 부터 도착지까지 걸리는 시간
        arrivalTime = getTargetBusTime(stationData,targetTime);
        for (int i = 0; i <= stationIndex; i++) {
            arrivalTime.addTime(stationTimes[i]);
        }
        return arrivalTime;
    }
}