package com.poputchic.android.reg_and_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

public class SignInOrRegistration extends Activity {

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
        checkSavedData();
    }

    private void checkSavedData() {
        driver = null;
        companion = null;
        String D = "";
        String C = "";
        Data data = new Data(this);
        try {
            D = data.getDriverData();
            C = data.getCompanionData();

            if (!D.equals("")||!C.equals("")){
                if (!D.equals("")){
                    createDriver(D);
                }
                if (!C.equals("")){
                    createCompanion(C);
                }
            }

        }catch (Exception e){
            //Log...
        }
        //Log.d(VARIABLES_CLASS.LOG_TAG,"DDD = " + driver);
        //Log.d(VARIABLES_CLASS.LOG_TAG,"CCC = " + companion);


    }

    private void createCompanion(String s) {
        companion = null;
        final Intent intent = new Intent(SignInOrRegistration.this,MainListActivity.class);
        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                companion = dataSnapshot.getValue(Companion.class);
                if (companion!=null){
                    intent.putExtra("companion",companion);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createDriver(String s) {
        driver = null;
        final Intent intent = new Intent(SignInOrRegistration.this,MainListActivity.class);
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver = dataSnapshot.getValue(Driver.class);
                if (driver!=null){
                    intent.putExtra("driver",driver);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
