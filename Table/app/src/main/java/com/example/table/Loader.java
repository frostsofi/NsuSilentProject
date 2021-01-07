package com.example.table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Loader
{
    private Document doc;
    private HashMap <String, ArrayList<StudyInfo>> week = new HashMap<>();

    private final String week_ = " неделя";
    private final String lecture_hall = "Ауд.";
    private final String BA_room = "БА";
    private final String MA_room = "МА";
    private final String mainCorps = "ГК";
    private final String labCorps = "ЛК";
    private final String URL = "https://table.nsu.ru/group/";
    private final String skippedString1 = "Google Play";
    private final String skippedString2 = "App Store";
    private final String typeOfCurrentWeek = "Нечетная";

    public void loadTable(String group)
    {
        try
        {
            doc = Jsoup.connect(URL  + group).timeout(20 * 1000).get();
        }
        catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    private String getDay(int ind)
    {
        String day = "";
        switch (ind) {
            case 0: { day =  "Monday";break; }
            case 1: { day =  "Tuesday";break;}
            case 2: { day = "Wednesday";break; }
            case 3: { day = "Thursday";break; }
            case 4: { day = "Friday";break; }
            case 5: {day =  "Saturday";break;}
        }
        return day;
    }

    private void initialWeek()
    {
        week.put("Monday", new ArrayList<StudyInfo>());
        week.put("Tuesday", new ArrayList<StudyInfo>());
        week.put("Wednesday", new ArrayList<StudyInfo>());
        week.put("Thursday", new ArrayList<StudyInfo>());
        week.put("Friday", new ArrayList<StudyInfo>());
        week.put("Saturday", new ArrayList<StudyInfo>());
    }

    private StudyInfo makeEmptyStudyInfo(String time_)      //it's need if in this time we don't have subject
    {
        StudyInfo studyInfo = new StudyInfo();
        studyInfo.setType("-");
        studyInfo.setTutor("-");
        studyInfo.setRoom("-");
        studyInfo.setName("-");
        studyInfo.setTime(time_);
        return studyInfo;
    }

    private StudyInfo isSubjectWindow(String text, String time_, int ind)
    {
        StudyInfo studyInfo = null;
        if (text.equals(""))
        {
            studyInfo = makeEmptyStudyInfo(time_);
            week.get(getDay(ind)).add(studyInfo);
            return  studyInfo;
        }
        return new StudyInfo();
    }

    private void findNameAndRoom(String[] params, int start, int length, StudyInfo studyInfo)
    {
        String name = "";
        String room_ = "";

        if (params[start].matches(lecture_hall))
        {
            for (int k = start; k < length; k++) {
                room_ = room_.concat(params[k] + " ");
            }
            name = "-";
            studyInfo.setRoom(room_);
        }
        else
        {
            studyInfo.setRoom(params[start]);

            if ((++start) < length) {
                if (params[start].equals(mainCorps) || params[start].equals(labCorps))
                    start++;
                for (int k = start; k < length; k++) {
                    name = name.concat(params[k]+ " ");
                }
            }
            else
                name = "-";
        }
        studyInfo.setTutor(name);
    }

    private String findSubject(String[] params, int start)
    {
        String subject_ = "";
        while (!(params[start].matches("[0-9]+")) && !(params[start].matches(lecture_hall))
                &&!(params[start].matches(".[0-9]+"))
                && !(params[start].matches(BA_room)) && !(params[start].matches(MA_room)))
        {
            subject_ = subject_.concat(params[start]+ " ");
            start++;
        }
        return  subject_;
    }


    private String findKindOfWeek(Elements header_)
    {
        final int parity = 4;
        initialWeek();
        int count = 0;
        String kindOfWeek = "";
        for (Element h : header_)
        {
            if (count == parity)
                kindOfWeek = h.text();
            count++;
        }
        kindOfWeek = kindOfWeek.replace(week_, "");
        return kindOfWeek;
    }

    private Elements findHeaderHTML()
    {
        Elements header = doc.getElementsByTag("header");
        return header.select("div");
    }

    public HashMap<String, ArrayList<StudyInfo>> parseTable()
    {
        Elements inputElements = doc.getElementsByTag("td");
        Elements header_ = findHeaderHTML();

        String kindOfWeek = findKindOfWeek(header_);
        int skip = 0, ind = 0;
        String time_ = "";
        for (Element inputElement : inputElements)
        {
            if (skip < 14) {
                skip++;
                continue;
            }

            String text = inputElement.text();
            if (text.equals(skippedString1) || text.equals(skippedString2) || (week.get("Monday").size() == 7 && ind == 6))
                continue;
            if (text.contains(":")) {
                time_ = text;
                continue;
            }
            if (ind == 6) ind = 0;

            StudyInfo studyInfo = isSubjectWindow(text, time_, ind);
            if (studyInfo.getName() != null) {
                ind++;
                continue;
            }

            String[] params = text.split(" ");
            int i = 0, start = 0, length = params.length;

            if (text.contains(kindOfWeek)) {
                while (!params[i].equals(typeOfCurrentWeek))
                    i++;
                if (!params[i].equals(kindOfWeek))
                    start = ++i;
                else
                    length = i;
            }

            studyInfo.setType(params[0]);
            studyInfo.setName(findSubject(params, ++start));
            findNameAndRoom(params, start, length, studyInfo);
            studyInfo.setTime(time_);
            week.get(getDay(ind)).add(studyInfo);
            ind++;
        }

        return week;
    }
}

