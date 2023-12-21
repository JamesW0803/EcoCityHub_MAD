package com.example.madassignment;

public class ActivityHelper {
    private String title;
    private String location;
    private String date;
    private String key;
    private String startTime;

    public ActivityHelper(String title, String location, String date, String key) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getKey(){
        return key;
    }
    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }
}