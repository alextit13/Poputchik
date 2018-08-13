package com.poputchic.android.models;

import java.io.Serializable;

public class Travel implements Serializable {
    private String from;
    private String to;
    private String time_from;
    private String time_to;
    private String driver_create;
    private String about_travel;
    private String time_create;
    private int places;
    private int companion;

    public Travel() {
    }

    public Travel(int companion, int places,String from, String to, String time_from, String time_to, String driver_create, String about_travel, String time_create) {
        this.companion = companion;
        this.places = places;
        this.from = from;
        this.to = to;
        this.time_from = time_from;
        this.time_to = time_to;
        this.driver_create = driver_create;
        this.about_travel = about_travel;
        this.time_create = time_create;
    }

    public int getCompanion() {
        return companion;
    }

    public void setCompanion(int companion) {
        this.companion = companion;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    public void setTime_to(String time_to) {
        this.time_to = time_to;
    }

    public String getDriver_create() {
        return driver_create;
    }

    public void setDriver_create(String driver_create) {
        this.driver_create = driver_create;
    }

    public String getAbout_travel() {
        return about_travel;
    }

    public void setAbout_travel(String about_travel) {
        this.about_travel = about_travel;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", time_from='" + time_from + '\'' +
                ", time_to='" + time_to + '\'' +
                ", driver=" + driver_create +
                ", about_travel='" + about_travel + '\'' +
                ", time_create='" + time_create + '\'' +
                ", places=" + places +
                '}';
    }
}