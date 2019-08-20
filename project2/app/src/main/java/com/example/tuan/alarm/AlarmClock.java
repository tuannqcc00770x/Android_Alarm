package com.example.tuan.alarm;

public class AlarmClock {
    private String hour;
    private String minute;
    private String ampm;
    private boolean toggle;
    private int id;

    public AlarmClock(String hour, String minute, String ampm, Boolean toggle, int id) {
        this.hour = hour;
        this.minute = minute;
        this.ampm = ampm;
        this.toggle = toggle;
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getAmpm() {
        return ampm;
    }

    public boolean getToggle() {
        return toggle;
    }

    public int getId() {
        return id;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }
    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }
}
