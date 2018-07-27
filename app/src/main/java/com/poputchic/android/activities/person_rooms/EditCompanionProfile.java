package com.poputchic.android.activities.person_rooms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Companion;

public class EditCompanionProfile extends Activity {

    private EditText edit_driver_name_c,edit_driver_email_c,edit_driver_year_c,edit_driver_phone_c,edit_driver_about_c;
    private Button b_driver_cancel_c,b_driver_save_c;
    private ProgressBar edit_companion;
    private FrameLayout container_companion_edit;

    private Companion companion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_companion_profile);

        init();

        changeFonts(); // изменение шрифта вьюх

        clicker();
        completeViews();
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,edit_driver_name_c);
        FontsDriver.changeFontToComfort(this,edit_driver_email_c);
        FontsDriver.changeFontToComfort(this,edit_driver_year_c);
        FontsDriver.changeFontToComfort(this,edit_driver_phone_c);
        FontsDriver.changeFontToComfort(this,edit_driver_about_c);
        FontsDriver.changeFontToComfort(this,b_driver_cancel_c);
        FontsDriver.changeFontToComfort(this,b_driver_save_c);
    }

    private void completeViews() {
        edit_driver_about_c.setText(companion.getAbout());
        edit_driver_email_c.setText(companion.getEmail());
        edit_driver_name_c.setText(companion.getName());
        edit_driver_phone_c.setText(companion.getPhone());
        edit_driver_year_c.setText(companion.getYear()+"");
    }

    private void clicker() {
        b_driver_cancel_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b_driver_save_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_companion_edit.setAlpha(.3f);
                edit_companion.setVisibility(View.VISIBLE);
                save();
            }
        });
    }

    private void save() {
        if (checkCompletedAllFields()){
            companion.setAbout(edit_driver_about_c.getText().toString());
            companion.setEmail(edit_driver_email_c.getText().toString());
            companion.setName(edit_driver_name_c.getText().toString());
            companion.setPhone(edit_driver_phone_c.getText().toString());
            companion.setYear(Integer.parseInt(edit_driver_year_c.getText().toString()));
            pushCompanionToFirebase();
        }else{
            Snackbar.make(b_driver_save_c,"Заполните все поля",Snackbar.LENGTH_LONG).show();
        }
    }

    private void pushCompanionToFirebase() {
        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(companion.getDate_create()+"")
                .setValue(companion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                edit_companion.setVisibility(View.INVISIBLE);
                container_companion_edit.setAlpha(1f);
                Toast.makeText(EditCompanionProfile.this, "Данные успешно сохранены!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean checkCompletedAllFields() {
        boolean b = false;
        if (!edit_driver_name_c.getText().toString().equals("")&&!edit_driver_email_c.getText().toString().equals("")&&
                !edit_driver_year_c.getText().toString().equals("")&&!edit_driver_phone_c.getText().toString().equals("")&&
                !edit_driver_about_c.getText().toString().equals("")){
            b = true;
        }
        return b;
    }

    private void init() {
        container_companion_edit = (FrameLayout) findViewById(R.id.container_companion_edit);
        edit_companion = (ProgressBar)findViewById(R.id.edit_companion);
        edit_companion.setVisibility(View.INVISIBLE);

        edit_driver_name_c = (EditText) findViewById(R.id.edit_driver_name_c);
        edit_driver_email_c = (EditText) findViewById(R.id.edit_driver_email_c);
        edit_driver_year_c = (EditText) findViewById(R.id.edit_driver_year_c);
        edit_driver_phone_c = (EditText) findViewById(R.id.edit_driver_phone_c);
        edit_driver_about_c = (EditText) findViewById(R.id.edit_driver_about_c);

        b_driver_cancel_c = (Button) findViewById(R.id.b_driver_cancel_c);
        b_driver_save_c = (Button) findViewById(R.id.b_driver_save_c);

        companion = (Companion) getIntent().getSerializableExtra("companion");
    }
}
