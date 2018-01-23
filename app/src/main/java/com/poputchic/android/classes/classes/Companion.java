package com.poputchic.android.classes.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Companion implements Serializable{

    private String about;
    private String date_create;
    private int year;
    private String image_path;
    private String email;
    private String password;
    private String name;
    private ArrayList<Travel>rewiews;

    public Companion() {
    }

    public Companion(String date_create, String email, String password, String name) {
        this.date_create = date_create;
        this.email = email;
        this.password = password;
        this.name = name;
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

    public ArrayList<Travel> getRewiews() {
        return rewiews;
    }

    public void setRewiews(ArrayList<Travel> rewiews) {
        this.rewiews = rewiews;
    }
}
