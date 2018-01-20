package com.poputchic.android.classes.classes;

import java.io.Serializable;

public class Review implements Serializable {
    private String time_create;
    private Users user_from_review;

    public Review(String time_create, Users user_from_review) {
        this.time_create = time_create;
        this.user_from_review = user_from_review;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public Users getUser_from_review() {
        return user_from_review;
    }

    public void setUser_from_review(Users user_from_review) {
        this.user_from_review = user_from_review;
    }
}
