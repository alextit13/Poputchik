package com.poputchic.android.reg_and_sign;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SignInOrRegistration extends AppCompatActivity {

    private Button     c_b_sign_in,c_b_registration;
    private Driver driver;
    private Companion companion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_registration);
        init();
    }

    private void init(){
        c_b_sign_in = (Button) findViewById(R.id.c_b_sign_in);
        c_b_registration = (Button) findViewById(R.id.c_b_registration);
        checkSharedPreference();
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.c_b_sign_in:
                 signIn();
                //gotochosemethod("sign_in");
                break;
            case R.id.c_b_registration:
                // reg
                //gotochosemethod("registration");
                Intent intent = new Intent(SignInOrRegistration.this,Registration.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void signIn() {
        Intent intent = new Intent(SignInOrRegistration.this,SignIn.class);
        startActivity(intent);
    }

    private void checkSharedPreference() {
        try {

            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(VARIABLES_CLASS.FILENAME)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                //Log.d(LOG_TAG, str);
                Gson gson = new Gson();
                try {
                    driver = gson.fromJson(str, Driver.class);
                    companion = gson.fromJson(str,Companion.class);
                }catch (Exception e){
                    //Log...
                }
            }


            if (driver!=null){
                Intent intent = new Intent(SignInOrRegistration.this,MainListActivity.class);
                intent.putExtra("driver", driver);
                startActivity(intent);
            }else if (companion!=null){
                Intent intent = new Intent(SignInOrRegistration.this,MainListActivity.class);
                intent.putExtra("companion",companion);
                startActivity(intent);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
