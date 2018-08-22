package com.poputchic.android.models.companion;

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
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.presenters.splashScreenPresenter.SplashScreenPresenter;
import com.poputchic.android.views.registration.Registration;

import java.io.Serializable;

public class Companion implements Serializable{

    private String about;
    private String date_create;
    private int year;
    private String image_path;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String cardNumber;

    private String city;
    private Data data;
    private String D = "";
    private Companion companion;
    private String priglos;

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

    public Companion(String about, String date_create, int year, String image_path, String email, String password, String name, String phone, String cardNumber, String priglos, int f) {
        this.about = about;
        this.date_create = date_create;
        this.year = year;
        this.image_path = image_path;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.cardNumber = cardNumber;
        this.priglos = priglos;
    }

    public Companion(String about, String date_create, int year, String image_path, String email, String password, String name, String phone, String cardNumber, String city) {
        this.about = about;
        this.date_create = date_create;
        this.year = year;
        this.image_path = image_path;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.cardNumber = cardNumber;
        this.city=city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Companion getCompanion() {
        return companion;
    }

    public void setCompanion(Companion companion) {
        this.companion = companion;
    }

    public String getPriglos() {
        return priglos;
    }

    public void setPriglos(String priglos) {
        this.priglos = priglos;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public void getSaveCompanion(final SplashScreenPresenter presenter, final Activity activity){
        data = new Data(activity);
        final TakeSAveCompanion takeSAveDriver = (TakeSAveCompanion)presenter;
        try {
            D = data.getCompanionData(); // пытаемся получить компаньона
        }catch (NullPointerException n){
            Log.d(VARIABLES_CLASS.LOG_TAG, "exception: " + n.getMessage());
        }
        if (!D.equals("")){
            // если нам пришла джейсонка компаньона не нулевая то мы создаем его
            // создаем компаньона из строки

            FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                    .child(D).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    companion = dataSnapshot.getValue(Companion.class);

                    if (companion!=null)
                        takeSAveDriver.getCompanion(companion);
                    else
                        takeSAveDriver.getCompanion(null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else
            takeSAveDriver.getCompanion(null);
    }

    public void registrationCompanion(final Activity activity, final Companion c){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("companion")
                .child(c.getDate_create() + "")
                .setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Data data = new Data(activity);
                data.saveSharedPreferenceCOMPANION(c);

                Intent intent = new Intent(activity, MainListActivity.class);
                intent.putExtra("companion", c);
                Toast.makeText(activity, "Вошел компаньон", Toast.LENGTH_SHORT).show();
                activity.startActivity(intent);
            }
        });
    }

    public interface TakeSAveCompanion{
        void getCompanion(Companion companion);
    }

}
