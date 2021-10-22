package com.example.busapp;

public class Search {
    public static String[] BinarySearch(String time, String[] timeTable) {
        int low = 0;
        int high = timeTable.length - 1;
        int mid;
        String[] neighborTime = new String[2];
        while(low <= high) {
            mid = (low + high) / 2;
            neighborTime[0] = timeTable[Math.max(mid - 1, 0)];
            neighborTime[1] = timeTable[mid];

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