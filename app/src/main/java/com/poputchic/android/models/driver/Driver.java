package com.poputchic.android.models.driver;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.models.Data;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.presenters.splashScreenPresenter.SplashScreenPresenter;
import com.poputchic.android.views.registration.Registration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private int numApply;
    private Data data;
    private String D = "";
    private Driver driver;
    private String city;
    private String priglos;

    public Driver() {
    }

    public Driver(String numberCard, String about, String date_create, int year, String image_path, String rating, String email, String password, String name, String name_car, String year_car, String numberPhone, ArrayList<Travel> active_travels, ArrayList<Travel> complete_travels, ArrayList<Travel> rewiews, String city, String priglos) {
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
        this.city = city;
        this.priglos=priglos;
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

    public Driver(String numberCard, String about, String date_create, int year, String image_path, String rating, String email, String password, String name, String name_car, String year_car, String numberPhone, ArrayList<Travel> active_travels, ArrayList<Travel> complete_travels, ArrayList<Travel> rewiews, int numApp) {
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
        this.numApply=numApp;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setRewiews(ArrayList<Travel> rewiews) {
        this.rewiews = rewiews;
    }

    public int getNumApply() {
        return numApply;
    }

    public void setNumApply(int numApply) {
        this.numApply = numApply;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriglos() {
        return priglos;
    }

    public void setPriglos(String priglos) {
        this.priglos = priglos;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImage_path() {
        return image_path;
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

    public ArrayList<Travel> getRewiews() {
        return rewiews;
    }

    @Override
    public String toString() {
        return "ВАС ОДОБРИЛИ КАК ПОПУТЧИКА!"+ "\n \n"+
                "О водителе: " + about + "\n" +
                "Рейтинг: " + rating + "\n" +
                "E-mail: " + email + "\n" +
                "Имя: " + name + "\n" +
                "Авто: " + name_car + "\n" +
                "Год авто: " + year_car + "\n" +
                "Номер телефона: " + numberPhone;
    }

    public void getSaveDriver(final SplashScreenPresenter presenter, final Activity activity){
        data = new Data(activity);
        final TakeSAveDriver takeSAveDriver = (TakeSAveDriver)presenter;
        try {
            D = data.getDriverData(); // пытаемся получить водителя
        }catch (NullPointerException n){
            Log.d(VARIABLES_CLASS.LOG_TAG, "exception: " + n.getMessage());
        }
        if (!D.equals("")){
            // если нам пришла джейсонка водителя не нулевая то мы создаем водителя
            // создаем водителя из строки

            FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                    .child(D).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    driver = dataSnapshot.getValue(Driver.class);

                    if (driver!=null)
                        takeSAveDriver.getDriver(driver);
                    else
                        takeSAveDriver.getDriver(null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
            takeSAveDriver.getDriver(null);
    }

    public boolean checkSingleDriver(List<String> emailsUsers, String email) {
        boolean b = true;
        for (String s : emailsUsers){
            if (s.equals(email)) b = false;
        }
        return b;
    }

    public boolean checkDriverCompleteFields(String email, String p_1, String p_2, String name) {
        return email.contains("@") && email.length() > 6 && p_1.equals(p_2) && name.length() > 0;
    }

    public interface TakeSAveDriver{
        void getDriver(Driver driver);
    }

    public void saveDriverToFirebase(final Activity activity, final Driver d){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("drivers")
                .child(d.getDate_create() + "")
                .setValue(d).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Data data = new Data(activity);
                data.saveSharedPreferenceDRIVER(d);

                Intent intent = new Intent(activity, MainListActivity.class);
                Toast.makeText(activity, "Вошел драйвер", Toast.LENGTH_SHORT).show();
                intent.putExtra("driver", d);
                activity.startActivity(intent);
            }
        });
    }

}
