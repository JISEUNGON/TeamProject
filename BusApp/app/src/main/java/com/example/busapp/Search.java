package com.example.busapp;

public class Search {
    public static String[] BinarySearch(String time, String[] timeTable) {
        int low = 0;
        int high = timeTable.length - 1;
        int mid;
        String[] neighborTime = new String[2];
        while(low <= high) {

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
            } else if (DateFormat.compare(timeTable[high], time) < 0){ // case 3
                neighborTime[0] = timeTable[high];
                neighborTime[1] = timeTable[Math.min(timeTable.length - 1, high + 1)];
            } else { //case 2
                neighborTime[0] = timeTable[low];
                neighborTime[1] = timeTable[high];
            }

            if (DateFormat.compare(timeTable[mid],time) == 0) { // BinarySearch HIT!
                // [time 직전에 출발했던 버스, time에 출발하는 버스]
                return new String[]{timeTable[Math.max(0, mid - 1)], timeTable[mid]};
            } else if(DateFormat.compare(timeTable[mid], time) > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // BinarySearch MISS!
        // [time 직전에 출발한 버스, time 이후에 출발할 버스]
        return neighborTime;
    }
}