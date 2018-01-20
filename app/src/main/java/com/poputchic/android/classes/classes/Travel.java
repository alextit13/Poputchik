package com.poputchic.android.classes.classes;

import java.io.Serializable;

public class Travel implements Serializable {
    private String from;
    private String to;
    private String time_from;
    private String time_to;
    private Driver driver;
    private String about_travel;
    private String time_create;

    public Travel(String from, String to, String time_from, String time_to, Driver driver, String about_travel, String time_create) {
        this.from = from;
        this.to = to;
        this.time_from = time_from;
        this.time_to = time_to;
        this.driver = driver;
        this.about_travel = about_travel;
        this.time_create = time_create;
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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
}
