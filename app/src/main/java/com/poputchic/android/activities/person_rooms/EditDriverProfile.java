package com.poputchic.android.activities.person_rooms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Driver;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EditDriverProfile extends Activity {

    private EditText edit_driver_name, edit_driver_email, edit_driver_year, edit_driver_phone, edit_driver_auto
            ,edit_driver_auto_year,edit_driver_card,edit_driver_about;
    private Button b_driver_cancel, b_driver_save;
    private Driver driver;
    private TextView hint_text_rating;
    private String rating = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_profile);
        init();
        clicker();
        takeDriver();
        checkAndSaveRating();
    }

    private void takeDriver() {
        try {
            driver = (Driver) getIntent().getSerializableExtra("driver");
        } catch (Exception e) {
            // Log...
        }

        if (driver != null) {
            if (driver.getName() != null) {
                edit_driver_name.setText(driver.getName());
            }
            if (driver.getEmail() != null) {
                edit_driver_email.setText(driver.getEmail());
            }
            if (driver.getYear() != 0) {
                edit_driver_year.setText(driver.getYear() + "");
            }
            if (driver.getNumberPhone() != null) {
                edit_driver_phone.setText(driver.getNumberPhone());
            }
            if (driver.getName_car() != null) {
                edit_driver_auto.setText(driver.getName_car());
            }
            if (driver.getAbout()!=null){
                edit_driver_about.setText(driver.getAbout());
            }
            if (driver.getNumberCard()!=null){
                edit_driver_card.setText(driver.getNumberCard());
            }
            if (driver.getYear_car()!=null){
                edit_driver_auto_year.setText(driver.getYear_car());
            }

        }
    }

    private void clicker() {
        b_driver_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        b_driver_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        changeDataDriver();
        changeDataInSharedPreferences();
        Log.d(VARIABLES_CLASS.LOG_TAG,"driver = " + driver);
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(driver.getDate_create() + "")
                .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDriverProfile.this, "Выши данные успешно сохранены!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void changeDataDriver() {
        driver.setAbout(edit_driver_about.getText().toString());
        driver.setEmail(edit_driver_email.getText().toString());
        driver.setName(edit_driver_name.getText().toString());
        driver.setName_car(edit_driver_auto.getText().toString());
        driver.setNumberCard(edit_driver_card.getText().toString());
        driver.setNumberPhone(edit_driver_phone.getText().toString());
        driver.setYear(Integer.parseInt(edit_driver_year.getText().toString()));
        driver.setYear_car(edit_driver_auto_year.getText().toString());
        driver.setRating(hint_text_rating.getText().toString());
    }

    private void changeDataInSharedPreferences() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(VARIABLES_CLASS.FILENAME, MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();
            String json = gson.toJson(driver);
            bw.write(json);
            // закрываем поток
            bw.close();
            //Log.d(MainActivity.LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        edit_driver_name = (EditText) findViewById(R.id.edit_driver_name);
        edit_driver_email = (EditText) findViewById(R.id.edit_driver_email);
        edit_driver_year = (EditText) findViewById(R.id.edit_driver_year);
        edit_driver_phone = (EditText) findViewById(R.id.edit_driver_phone);
        edit_driver_auto = (EditText) findViewById(R.id.edit_driver_auto);
        edit_driver_auto_year = (EditText) findViewById(R.id.edit_driver_auto_year);
        edit_driver_card = (EditText) findViewById(R.id.edit_driver_card);
        edit_driver_about = (EditText) findViewById(R.id.edit_driver_about);

        b_driver_cancel = (Button) findViewById(R.id.b_driver_cancel);
        b_driver_save = (Button) findViewById(R.id.b_driver_save);

        hint_text_rating = (TextView) findViewById(R.id.hint_text_rating);
    }

    private void checkAndSaveRating() {

        //Log.d(VARIABLES_CLASS.LOG_TAG,"driver" + driver);

        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(driver
                .getDate_create()+"").child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rating = dataSnapshot.getValue().toString();
                        hint_text_rating.setText(rating);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
