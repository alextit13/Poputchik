package com.poputchic.android.classes.classes;

import java.util.ArrayList;

public class Driver extends Users{
    private String name_car;
    private String year_car;
    private ArrayList<Travel>active_travels;
    private ArrayList<Travel>complete_travels;

    public Driver(String email, String name, long date_of_create, String image_path, ArrayList<Review> list_review, String rating, String year_old, String about_me, String number_of_phone, String name_car, String year_car, ArrayList<Travel> active_travels, ArrayList<Travel> complete_travels) {
        super(email, name, date_of_create, image_path, list_review, rating, year_old, about_me, number_of_phone);
        this.name_car = name_car;
        this.year_car = year_car;
        this.active_travels = active_travels;
        this.complete_travels = complete_travels;
    }

    public String getName_car() {
        return name_car;
    }

    public void setName_car(String name_car) {
        this.name_car = name_car;
    }

    public String getYear_car() {
        return year_car;
    }

    public void setYear_car(String year_car) {
        this.year_car = year_car;
    }

    public ArrayList<Travel> getActive_travels() {
        return active_travels;
    }

    public void setActive_travels(ArrayList<Travel> active_travels) {
        this.active_travels = active_travels;
    }

    public ArrayList<Travel> getComplete_travels() {
        return complete_travels;
    }

    public void setComplete_travels(ArrayList<Travel> complete_travels) {
        this.complete_travels = complete_travels;
    }
}
