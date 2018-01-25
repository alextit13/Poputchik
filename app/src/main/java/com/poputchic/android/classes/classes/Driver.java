package com.poputchic.android.classes.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable{

    private String numberCard;
    private String about;
    private String date_create;
    private int year;
    private String image_path;
    private String rating;
    private String email;
    private String password;
    private String name;
    private String name_car;
    private String year_car;
    private String numberPhone;
    private ArrayList<Travel>active_travels;
    private ArrayList<Travel>complete_travels;
    private ArrayList<Travel>rewiews;

    public Driver() {
    }

    public Driver(String numberCard, String about, String date_create, int year, String image_path, String rating, String email, String password, String name, String name_car, String year_car, String numberPhone, ArrayList<Travel> active_travels, ArrayList<Travel> complete_travels, ArrayList<Travel> rewiews) {
        this.numberCard = numberCard;
        this.about = about;
        this.date_create = date_create;
        this.year = year;
        this.image_path = image_path;
        this.rating = rating;
        this.email = email;
        this.password = password;
        this.name = name;
        this.name_car = name_car;
        this.year_car = year_car;
        this.numberPhone = numberPhone;
        this.active_travels = active_travels;
        this.complete_travels = complete_travels;
        this.rewiews = rewiews;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Travel> getRewiews() {
        return rewiews;
    }

    public void setRewiews(ArrayList<Travel> rewiews) {
        this.rewiews = rewiews;
    }
}
