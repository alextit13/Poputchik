package com.poputchic.android.classes.classes;

import java.util.ArrayList;

public class Companion extends Users{

    private String lon;
    private String lat;

    public Companion(String email, String name, long date_of_create, String image_path, ArrayList<Review> list_review, String rating, String year_old, String about_me, String number_of_phone, String lon, String lat) {
        super(email, name, date_of_create, image_path, list_review, rating, year_old, about_me, number_of_phone);
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
