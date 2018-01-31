package com.poputchic.android.classes.classes;

import java.io.Serializable;

public class Zayavka implements Serializable{
    String dateCreate;
    String driver;
    String companion;
    String travel;
    String cost;

    public Zayavka() {
    }

    public Zayavka(String cost, String dateCreate, String driver, String companion, String travel) {
        this.cost = cost;
        this.dateCreate = dateCreate;
        this.driver = driver;
        this.companion = companion;
        this.travel = travel;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
