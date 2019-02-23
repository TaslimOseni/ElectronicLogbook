package com.dabinu.app.electroniclogbook.models;

/**
 * Created by Taslim Oseni on 2/19/19.
 */
public class Log{

    public String week, day, activity, comment;
    public boolean filledAlready;


    public Log(String week, String day, String activity, String comment, boolean filledAlready){
        this.week = week;
        this.day = day;
        this.activity = activity;
        this.comment = comment;
        this.filledAlready = filledAlready;
    }

    public Log(){

    }


    public String getWeek() {
        return week;
    }

    public String getDay() {
        return day;
    }

    public String getActivity() {
        return activity;
    }

    public String getComment() {
        return comment;
    }

    public boolean isFilledAlready() {
        return filledAlready;
    }
}