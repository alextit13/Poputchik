package com.poputchic.android.reg_and_sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.poputchic.android.R;

public class SignInOrRegistration extends AppCompatActivity {

    private Button     c_b_sign_in,c_b_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_registration);
        init();
    }

    private void init(){
        c_b_sign_in = (Button) findViewById(R.id.c_b_sign_in);
        c_b_registration = (Button) findViewById(R.id.c_b_registration);
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.c_b_sign_in:
                // signIn
                gotochosemethod("sign_in");
                break;
            case R.id.c_b_registration:
                // reg
                gotochosemethod("registration");
                break;
            default:
                break;
        }
    }

    private void gotochosemethod(String key) {
        Intent intent;
        switch (key){
            case "sign_in":
                intent = new Intent(SignInOrRegistration.this,SignIn.class);
                startActivity(intent);
                break;
            case "registration":
                intent = new Intent(SignInOrRegistration.this,Registration.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
