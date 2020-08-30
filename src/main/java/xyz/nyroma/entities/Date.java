package xyz.nyroma.entities;

import java.util.Calendar;

public class Date {
    private int day;
    private int month;
    private int year;
    private int hours;
    private int minutes;
    private int seconds;

    public Date(int deltaHours){
        Calendar calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.hours = calendar.get(Calendar.HOUR) + deltaHours;
        this.minutes = calendar.get(Calendar.MINUTE);
        this.seconds = calendar.get(Calendar.SECOND);
    }

    public int getDay() {
        return day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getMonth() {
        return month;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getYear() {
        return year;
    }
}
