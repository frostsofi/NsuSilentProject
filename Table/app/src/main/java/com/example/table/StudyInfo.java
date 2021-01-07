package com.example.table;

import java.io.Serializable;

public class StudyInfo implements Serializable
{
    private String type;
    private String name;
    private String time;
    private String tutor;
    private String room;

    public String getType()      //return if name lecture or practice
    {
        return type;
    }

    public String getName() { return name; }

    public  String getTime() { return time; }

    public String getTutor() { return tutor; }

    public String getRoom() { return room; }

    public void setType(String type) {this.type = type; }

    public void setName(String name) { this.name = name; }

    public  void setTime(String time) { this.time = time; }

    public void setTutor(String tutor) {this.tutor = tutor; }

    public void setRoom(String room) { this.room = room; }
}

