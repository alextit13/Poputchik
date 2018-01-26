package com.poputchic.android.reg_and_sign;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class Registration extends Activity {

    private FrameLayout a_fl_container;
    private EditText    a_et_email,a_et_password,a_et_password_confirm,a_et_name,a_et_number_of_phone;
    private RadioButton a_cb_driver,a_cb_companion;
    private Button      a_b_registration;
    private ImageView   a_iv_back;
    private String      image_path;
    private Companion   companion;
    private int         ID = 0;
    private Driver      driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        a_fl_container = (FrameLayout) findViewById(R.id.a_fl_container);

        a_et_email = (EditText)findViewById(R.id.a_et_email);
        a_et_password = (EditText)findViewById(R.id.a_et_password);
        a_et_password_confirm = (EditText)findViewById(R.id.a_et_password_confirm);
        a_et_name = (EditText)findViewById(R.id.a_et_name);
        a_et_number_of_phone = (EditText)findViewById(R.id.a_et_number_of_phone);

        a_cb_driver = (RadioButton)findViewById(R.id.a_cb_driver);
        a_cb_companion = (RadioButton)findViewById(R.id.a_cb_companion);

        a_b_registration = (Button)findViewById(R.id.a_b_registration);

        a_iv_back = (ImageView)findViewById(R.id.a_iv_back);

        ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.a_iv_back:
                // back
                closeApplication();
                break;
            case R.id.a_b_registration:
                //registration
                registration();
                break;
            default:
                break;
        }
    }

    private void registration() {
        if (a_cb_driver.isChecked()){
            if (!a_et_email.getText().toString().equals("")&&
                    !a_et_password.getText().toString().equals("")
                    && a_et_password.getText().toString().equals(a_et_password_confirm.getText()
            .toString())&&!a_et_name.getText().toString().equals("")){
                ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
                driver = new Driver("0000 0000 0000 0000","Обо мне",new Date().getTime()+"",25,
                        "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png","0",
                        a_et_email.getText().toString(),a_et_password.getText().toString()
                        ,a_et_name.getText().toString(),"Авто","1",a_et_number_of_phone
                        .getText().toString(),new ArrayList<Travel>(),new ArrayList<Travel>(),
                        new ArrayList<Travel>());
                saveToFirebaseDriver();
            }
        }else if (a_cb_companion.isChecked()){
            if (!a_et_email.getText().toString().equals("")&&
                    !a_et_password.getText().toString().equals("")
                    && a_et_password.getText().toString().equals(a_et_password_confirm.getText()
                    .toString())){
                ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
                companion = new Companion(new Date().getTime()+""
                        ,a_et_email.getText().toString(),a_et_password.getText().toString()
                ,a_et_name.getText().toString());
                saveToFirebaseCompanion();
            }
        }
    }

    private void saveToFirebaseCompanion() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("companion")
                .child(companion.getDate_create()+"")
                .setValue(companion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveSharedPreferenceCOMPANION(companion);
                Intent intent = new Intent(Registration.this,MainListActivity.class);
                intent.putExtra("companion",companion);
                startActivity(intent);
            }
        });
    }

    private void saveSharedPreferenceCOMPANION(Companion companion) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("FILENAME", MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();
            String json = gson.toJson(companion.getDate_create());


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

    private void saveToFirebaseDriver() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("drivers")
                .child(driver.getDate_create()+"")
                .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveSharedPreferenceDRIVER(driver);
                Intent intent = new Intent(Registration.this,MainListActivity.class);
                intent.putExtra("driver",driver);
                startActivity(intent);
            }
        });
    }

    private void saveSharedPreferenceDRIVER(Driver d) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(VARIABLES_CLASS.FILENAME, MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();
            String json = gson.toJson(d);


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

    private void closeApplication() {
        finish();
    }
}
