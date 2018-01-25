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
            final Driver[] driver = {null};
            final Companion[] companion = {null};

            String D = "";
            String C = "";
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("FILENAME")));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                //Log.d(LOG_TAG, str);
                Gson gson = new Gson();
                try {
                    D = gson.fromJson(str, String.class);
                    C = gson.fromJson(str,String.class);
                }catch (Exception e){
                    //Log...
                }

                if (!D.equals("")){
                    FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                            .child(D).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            driver[0] = dataSnapshot.getValue(Driver.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else if (!C.equals("")){
                    FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                            .child(D).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            companion[0] = dataSnapshot.getValue(Companion.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
            if (driver[0] !=null){
                //Log.d(MainActivity.LOG_TAG,"user = " + user);
                Intent intent = new Intent(SignInOrRegistration.this,MainListActivity.class);
                intent.putExtra("driver", driver[0]);
                startActivity(intent);
            }else if (companion[0]!=null){
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
