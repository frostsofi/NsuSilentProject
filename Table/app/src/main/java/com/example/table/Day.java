package com.example.table;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.table.ActionFunctional.TimeInterval;
import java.util.ArrayList;

public class Day implements Parcelable
{
     private ArrayList<TimeInterval> intervals;

     Day()
     {
         intervals = new ArrayList<>();
     }

    protected Day(Parcel in)
    {
        intervals = (ArrayList<TimeInterval>)in.readSerializable();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };


     public void setInterval(TimeInterval iInterval)
     {
         intervals.add(iInterval);
     }

     public ArrayList<TimeInterval> getIntervals()
     {
         return intervals;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(intervals);
    }
}
