package com.example.busapp;

import android.util.Log;

import java.util.Arrays;

public class Search {
    /**
     * time이 주어지면, timeTable에서 값을 검색합니다.
     * Naive BinarySearch와 다르게, 값이 리스트에 없는 경우 인접한 값을 되돌려 줍니다.
     *
     * @param time      찾으려는 시간
     * @param timeTable 참조하는 시간
     * @return String[time 직전에 출발했던 버스, time에 출발하는 버스]
     */
    public static String[] FindClosestBus(String time, String[] timeTable) {
        int low = 0;
        int high = timeTable.length - 1;
        int mid;
        String[] neighborTime = new String[2];
        while (low <= high) {

            mid = (low + high) / 2;

            /**
             * BinarySearch Miss 대비
             * 결국 최종 케이스는 3가지 중 하나,
             *   1. time < timeTable[low] <= timeTable[high]
             *   2. timeTable[low] < time < timeTable[high]
             *   3. timeTable[low] <= timeTable[high] < time
             */
            if (DateFormat.compare(timeTable[low], time) > 0) { // case 1
                neighborTime[0] = timeTable[Math.max(0, low - 1)];
                neighborTime[1] = timeTable[low];
            } else if (DateFormat.compare(timeTable[high], time) < 0) { // case 3
                neighborTime[0] = timeTable[high];
                neighborTime[1] = timeTable[Math.min(timeTable.length - 1, high + 1)];
            } else { //case 2
                neighborTime[0] = timeTable[low];
                neighborTime[1] = timeTable[high];
            }

            if (DateFormat.compare(timeTable[mid], time) == 0) { // BinarySearch HIT!
                // [time 직전에 출발했던 버스, time에 출발하는 버스]
                return new String[]{timeTable[Math.max(0, mid - 1)], timeTable[mid]};
            } else if (DateFormat.compare(timeTable[mid], time) > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // BinarySearch MISS!
        // [time 직전에 출발한 버스, time 이후에 출발할 버스]
        return neighborTime;
    }

    /**
     * targetStation이 stationTable에 속하는지 확인합니다.
     *
     * @param target 찾으려는 값
     * @param table  참조하는 배열
     *               * @return true : 정류장이 있는 경우
     *               false: 정류장이 없는 경우
     */
    public static boolean hasTarget(String target, String[] table) {
        String[] sortedTable = QuickSort.sort(table, String::compareTo);
        int low = 0;
        int high = table.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
            if (target.equals(sortedTable[mid])){
                return true;
            }else{
                //검색 대상값이 비교대상과 비교하여 -인 경우 (사전순으로 앞에있는 경우)
                if (target.compareTo(sortedTable[mid]) < 0) {
                    high = mid -1;
                }
                //검색 대상값이 비교대상과 비교하여 +인 경우 (사전순으로 뒤에 있는경우)
                else{
                    low = mid + 1;
                }
            }
        }
        //찾지 못했다면
        return false;
    }
}