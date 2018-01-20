package com.poputchic.android.reg_and_sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

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
        if (checkUser()){
            if (driver!=null){
                Intent intent = new Intent(SignIn.this,MainListActivity.class);
                intent.putExtra("driver",driver);
                startActivity(intent);
            }else if (companion!=null){
                Intent intent = new Intent(SignIn.this,MainListActivity.class);
                intent.putExtra("companion",companion);
                startActivity(intent);
            }

        }
    }

    private boolean checkUser() {
        final boolean[] check = {false};
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //list_drivers.add(postSnapshot.getValue(Driver.class));
                    if (postSnapshot.getValue(Driver.class).getEmail().equals(b_et_email.getText().toString())&&
                            postSnapshot.getValue(Driver.class).getPassword().equals(b_et_password.getText().toString())){
                        check[0] = true;
                        driver = postSnapshot.getValue(Driver.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("users").child("companion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //list_drivers.add(postSnapshot.getValue(Driver.class));
                    if (postSnapshot.getValue(Companion.class).getEmail().equals(b_et_email.getText().toString())&&
                            postSnapshot.getValue(Driver.class).getPassword().equals(b_et_password.getText().toString())){
                        check[0] = true;
                        companion = postSnapshot.getValue(Companion.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check[0];
    }
}
