package com.poputchic.android.reg_and_sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SignIn extends AppCompatActivity {

    private EditText    b_et_email,b_et_password;
    private Button      b_b_sign_in;
    private ImageView   b_iv_back;

    private Driver driver;
    private Companion companion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();

    }

    private void init(){
        b_et_email = (EditText) findViewById(R.id.b_et_email);
        b_et_password = (EditText) findViewById(R.id.b_et_password);

        b_b_sign_in = (Button) findViewById(R.id.b_b_sign_in);
        b_iv_back = (ImageView) findViewById(R.id.b_iv_back);
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.b_b_sign_in:
                // sign in
                signIn();
                break;
            case R.id.b_iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        //Log.d(VARIABLES_CLASS.LOG_TAG,"1");
        if (!b_et_email.getText().toString().equals("")
                &&!b_et_password.getText().toString().equals("")){
            //Log.d(VARIABLES_CLASS.LOG_TAG,"2");
            FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                Log.d(VARIABLES_CLASS.LOG_TAG,"3");
                                if (postSnapshot.getValue(Companion.class).getEmail().equals(b_et_email.getText().toString())
                                        &&
                                        postSnapshot.getValue(Companion.class).getPassword().equals(b_et_password.getText().toString())){
                                    //Log.d(VARIABLES_CLASS.LOG_TAG,"4");
                                    saveSharedPreferenceCOMPANION(postSnapshot.getValue(Companion.class));
                                    Intent intent = new Intent(SignIn.this,MainListActivity.class);
                                    intent.putExtra("companion",postSnapshot.getValue(Companion.class));
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                if (postSnapshot.getValue(Driver.class).getEmail().equals(b_et_email.getText().toString())
                                        &&
                                        postSnapshot.getValue(Driver.class).getPassword().equals(b_et_password.getText().toString())){
                                    saveSharedPreferenceDRIVER(postSnapshot.getValue(Driver.class));
                                    Intent intent = new Intent(SignIn.this,MainListActivity.class);
                                    intent.putExtra("driver",postSnapshot.getValue(Driver.class));
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void saveSharedPreferenceCOMPANION(Companion companion) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("FILENAME", MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();
            String json = gson.toJson(companion);


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

    private void saveSharedPreferenceDRIVER(Driver driver) {
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

}
