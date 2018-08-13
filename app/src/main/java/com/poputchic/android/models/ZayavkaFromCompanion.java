package com.poputchic.android.models;

import java.io.Serializable;

public class ZayavkaFromCompanion extends Zayavka implements Serializable{
    String date;
    String from_location;
    String to_location;
    String from_time;
    String to_time;
    String price;
    String companion;
    String driver;

    public ZayavkaFromCompanion() {
    }

    public ZayavkaFromCompanion(String driver, String date, String from_location, String to_location, String from_time, String to_time, String price, String companion) {
        this.driver = driver;
        this.date = date;
        this.from_location = from_location;
        this.to_location = to_location;
        this.from_time = from_time;
        this.to_time = to_time;
        this.price = price;
        this.companion = companion;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom_location() {
        return from_location;
    }

    public void setFrom_location(String from_location) {
        this.from_location = from_location;
    }

    public String getTo_location() {
        return to_location;
    }

    public void setTo_location(String to_location) {
        this.to_location = to_location;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }
}
