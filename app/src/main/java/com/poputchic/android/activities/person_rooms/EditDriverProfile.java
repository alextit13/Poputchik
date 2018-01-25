package com.poputchic.android.activities.person_rooms;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Driver;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EditDriverProfile extends AppCompatActivity {

    private EditText edit_driver_name, edit_driver_email, edit_driver_year, edit_driver_phone, edit_driver_auto;
    private Button b_driver_cancel, b_driver_save;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_profile);
        init();
        clicker();
        takeDriver();
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
        changeDataInSharedPreferences();
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(driver.getDate_create() + "")
                .setValue(driver)/*.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDriverProfile.this, "Выши данные успешно сохранены!", Toast.LENGTH_SHORT).show();
                finish();
            }
        })*/;

    }

    private void changeDataInSharedPreferences() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("FILENAME", MODE_PRIVATE)));
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

        b_driver_cancel = (Button) findViewById(R.id.b_driver_cancel);
        b_driver_save = (Button) findViewById(R.id.b_driver_save);
    }
}
