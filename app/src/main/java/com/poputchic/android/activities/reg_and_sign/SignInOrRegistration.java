package com.poputchic.android.activities.reg_and_sign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

public class SignInOrRegistration extends Activity {

    private Button     c_b_sign_in,c_b_registration;

    //private ProgressBar progress_bar_sign_or_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_registration);
        init(); // инициализация вьюх
        changeFonts(); // изменяем шрифт на вьюхах
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,c_b_registration);
        FontsDriver.changeFontToComfort(this,c_b_sign_in);
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.c_toolbar));
    } // change viws on views

    private void init(){
        c_b_sign_in = (Button) findViewById(R.id.c_b_sign_in);
        c_b_registration = (Button) findViewById(R.id.c_b_registration);
        //progress_bar_sign_or_reg = (ProgressBar) findViewById(R.id.progress_bar_sign_or_reg);
        //progress_bar_sign_or_reg.setVisibility(View.VISIBLE);
    }


    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.c_b_sign_in:
                next(SignIn.class);
                break;
            case R.id.c_b_registration:
                next(Registration.class);
                break;
            default:
                break;
        }
    }

    private void next(Class className) {
        Intent intent = new Intent(SignInOrRegistration.this,className);
        startActivity(intent);
    }

    public void clickDocument(View view) {
        new AlertDialog.Builder(this).setTitle("Правила").setMessage(R.string.message)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    } // кликаем на кнпку документа
}
