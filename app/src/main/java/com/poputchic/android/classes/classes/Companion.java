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
    private String phone;

    public Companion() {
    }

    public Companion(String about, String date_create, int year, String image_path, String email, String password, String name, String phone) {
        this.about = about;
        this.date_create = date_create;
        this.year = year;
        this.image_path = image_path;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Companion{" +
                "about='" + about + '\'' +
                ", date_create='" + date_create + '\'' +
                ", year=" + year +
                ", image_path='" + image_path + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
