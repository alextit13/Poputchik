package com.poputchic.android.classes.classes;

import java.io.Serializable;

public class Review implements Serializable {
    private String date;
    private String from;
    private String to;
    private String text;

    public Review(String date, String from, String to, String text) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
