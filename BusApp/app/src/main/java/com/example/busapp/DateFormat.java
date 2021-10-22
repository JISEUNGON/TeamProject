package com.example.busapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    int totalMin;
    String timeFormat;
    //DateFormat으로 받은 HH:MM format을 비교할 수 있도록 분으로 변환한다.
    //08:00 = 8 x 60 = 480 , 12:30 = 12 X 60 + 30 = 750, 14:20 = 14 X 60 + 20 = 860 ...
    public DateFormat(String date){
        timeFormat = date;
        String[] time = date.split(":");
        totalMin = Integer.parseInt(time[0])*60 + Integer.parseInt(time[1]);
    }
    //파라메터가 없을 경우 현재 시간으로 데이터를 생성한다.
    public DateFormat(){
        long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(now);
        timeFormat = format.format(date);
        String[] time = timeFormat.split(":");
        totalMin = Integer.parseInt(time[0])*60 + Integer.parseInt(time[1]);
    }

    //분 형태의 시간을 반환한다.
    public int getTotalMin() {
        return totalMin;
    }
    //xx:xx 포멧 형태의 시간을 반환한다.
    public String getTime(){
        return timeFormat;
    }

    //파라메터로 주어진 시간을 더한다.
    public void addTime(int time){
        totalMin = totalMin + time;
        int hour = totalMin/60;
        int min = totalMin - hour*60;
        if(min<10){
            timeFormat = hour + ":0" + min;
        }
        else{
            timeFormat = hour + ":" + min;
        }
    }

    /**
     * 객체 생성하지 않고 비교하기 위한 compare
     * @param src String
     * @param target String
     * @return  양수  if src > target
     *           0   if src == target
     *          음수  if src < target
     */
    public static int compare(String src, String target) {
        String[] srcArray = src.split(":");
        int srcTotalMin = Integer.parseInt(srcArray[0]) * 60 + Integer.parseInt(srcArray[1]);

        String[] targetArray = target.split(":");
        int targetTotalMin = Integer.parseInt(targetArray[0]) * 60 + Integer.parseInt(targetArray[1]);

        return srcTotalMin - targetTotalMin;
    }
}