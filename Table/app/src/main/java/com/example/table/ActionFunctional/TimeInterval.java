package com.example.table.ActionFunctional;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class TimeInterval implements Comparable <TimeInterval>, Serializable
{
    public class Time implements Comparable <Time>
    {
        private int hour;
        private int minutes;

        public Time(int iHour, int iMinutes)
        {
            hour = iHour;
            minutes = iMinutes;
        }

        public int getHours()
        {
            return hour;
        }

        public int getMinutes()
        {
            return minutes;
        }

        @Override
        public int compareTo(Time o) {
            return this.hour > o.hour ? 1: -1;
        }
    }
    private Time timeStart;                       //time when action start in minutes
    private Time timeEnd;                         //time when action end in minutes

    public TimeInterval(String timeStart)
    {
        int[] timeStartArr = timeToInt(timeStart);
        this.timeStart = new Time(timeStartArr[0], timeStartArr[1]);
    }

    public void setTimeEnd(String timeEnd)
    {
        int[] timeEndArr = timeToInt(timeEnd);
        this.timeEnd = new Time(timeEndArr[0], timeEndArr[1]);
    }

    private int[] timeToInt(String time)
    {
        String[] timeArray = time.split(":");
        int[] timeInt = new int[2];
        timeInt[0] = Integer.valueOf(timeArray[0]);
        timeInt[1] = Integer.valueOf(timeArray[1]);

        return timeInt;
    }

    public Time getTimeStart()
    {
        return timeStart;
    }

    public Time getTimeEnd()
    {
        return timeEnd;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(TimeInterval o) {
        return timeStart.compareTo(o.getTimeStart());
    }
}
