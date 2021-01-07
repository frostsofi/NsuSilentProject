package com.example.table;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.example.table.ActionFunctional.TimeInterval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableActivity extends Activity
{
    private String group;
    private Loader loader = new Loader();
    private HashMap<String, Day> table;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        table = new HashMap<String, Day>();
        group = getIntent().getStringExtra("group");
    }

    @Override
    public void onStart() {

        super.onStart();

        Thread net = new Thread(new Connection());
        net.start();
        synchronized (table)
        {
            try {
                table.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printTable();
    }

    private void printTable()
    {
        System.out.println("\n____________________________________________________-");
        for (Map.Entry<String, Day> table_ : table.entrySet())
        {
            System.out.println(table_.getKey());
            for (TimeInterval interval : table_.getValue().getIntervals())
            {
                System.out.println("START_HOURS: " + interval.getTimeStart().getHours());
                System.out.println("START_MINUTES: " + interval.getTimeStart().getMinutes());
                System.out.println("END_HOURS: " + interval.getTimeEnd().getHours());
                System.out.println("END_MINUTES: " + interval.getTimeEnd().getMinutes());
            }

            System.out.println("\n____________________________________________________-");
        }
    }
    class Connection implements Runnable
    {
        @Override
        public void run()
        {
            loader.loadTable(group);
            HashMap<String, ArrayList<StudyInfo>> info;
            info = loader.parseTable();

            for (Map.Entry<String, ArrayList<StudyInfo>> subj : info.entrySet())
            {
                Day day = new Day();
                boolean intervalStart = false;
                TimeInterval curInterval = null;

                for (int i = 0; i  < subj.getValue().size(); i++)
                {
                    StudyInfo st = subj.getValue().get(i);
                    if (st.getName().equals("-"))
                    {
                        intervalStart = false;

                        if (curInterval != null)
                        {
                            curInterval.setTimeEnd(st.getTime());
                            day.setInterval(curInterval);
                            curInterval = null;
                        }
                        continue;
                    }

                    if (!intervalStart) {
                        curInterval = new TimeInterval(st.getTime());
                        intervalStart = true;
                    }
                }

                table.put(subj.getKey(), day);
            }

            synchronized (table)
            {
                table.notify();
            }
        }
    }
}
