package com.poputchic.android.reg_and_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

public class SignIn extends Activity {

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
        cleanData();
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
        if (checkCompleteFields()){
            driver = getDataDriver();
            companion = getDataCompanion();
        }
    }

    private void cleanData() {
        Data data = new Data(this);
        data.saveSharedPreferenceDRIVER(null);
        data.saveSharedPreferenceCOMPANION(null);
    }

    private boolean checkCompleteFields() {
        boolean b = false;
        if (!b_et_email.getText().toString().equals("")
                &&!b_et_password.getText().toString().equals("")){
            b = true;
        }else{
            b = false;
            Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
        }
        return b;
    }

    public Driver getDataDriver() {
        driver = null;
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child("drivers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        if (data.getValue(Driver.class).getEmail().equals(b_et_email.getText().toString())
                                &&data.getValue(Driver.class).getPassword().equals(b_et_password.getText().toString())){
                            Log.d(VARIABLES_CLASS.LOG_TAG,"dr = " + data.getValue(Driver.class));
                            driver = data.getValue(Driver.class);
                            goToIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
        return driver;
    }

    private void goToIntent() {
        if (driver!=null){
            Intent intent = new Intent(SignIn.this,MainListActivity.class);
            intent.putExtra("driver",driver);
            startActivity(intent);
        }
        if (companion!=null){
            Intent intent = new Intent(SignIn.this,MainListActivity.class);
            intent.putExtra("companion",companion);
            startActivity(intent);
        }
    }

    public Companion getDataCompanion() {
        companion = null;
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child("companion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        if (data.getValue(Companion.class).getEmail().equals(b_et_email.getText().toString())
                                &&data.getValue(Companion.class).getPassword().equals(b_et_password.getText().toString())){
                            companion = data.getValue(Companion.class);
                            goToIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
        return companion;
    }
}
