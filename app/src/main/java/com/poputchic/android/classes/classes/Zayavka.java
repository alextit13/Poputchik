package com.poputchic.android.classes.classes;

import java.io.Serializable;

public class Zayavka implements Serializable{
    String dateCreate;
    String driver;
    String companion;
    String travel;

    public Zayavka() {
    }

    public Zayavka(String dateCreate, String driver, String companion, String travel) {
        this.dateCreate = dateCreate;
        this.driver = driver;
        this.companion = companion;
        this.travel = travel;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }
}
