package com.poputchic.android.classes.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable{
    private String password;
    private String email;
    private String name;
    private long date_of_create;
    private String image_path;
    private ArrayList<Review>list_review;
    private String rating;
    private String year_old;
    private String about_me;
    private String number_of_phone;

    public Users(String password, String email, String name, long date_of_create, String image_path, ArrayList<Review> list_review, String rating, String year_old, String about_me, String number_of_phone) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.date_of_create = date_of_create;
        this.image_path = image_path;
        this.list_review = list_review;
        this.rating = rating;
        this.year_old = year_old;
        this.about_me = about_me;
        this.number_of_phone = number_of_phone;
    }

    public String getNumber_of_phone() {
        return number_of_phone;
    }

    public void setNumber_of_phone(String number_of_phone) {
        this.number_of_phone = number_of_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate_of_create() {
        return date_of_create;
    }

    public void setDate_of_create(long date_of_create) {
        this.date_of_create = date_of_create;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public ArrayList<Review> getList_review() {
        return list_review;
    }

    public void setList_review(ArrayList<Review> list_review) {
        this.list_review = list_review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear_old() {
        return year_old;
    }

    public void setYear_old(String year_old) {
        this.year_old = year_old;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }
}
