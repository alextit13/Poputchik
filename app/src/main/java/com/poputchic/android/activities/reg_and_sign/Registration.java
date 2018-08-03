package com.poputchic.android.activities.reg_and_sign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Registration extends Activity implements UploadAllUsersFromServer.AllUsersFromServer {

    private EditText a_et_email, a_et_password, a_et_password_confirm, a_et_name, a_et_number_of_phone;
    private RadioButton a_cb_driver, a_cb_companion;
    private ImageView image_back_registration;
    private Companion companion;
    private Driver driver;
    private ScrollView scroll_reg;
    private boolean b = false;
    private List<String>usersEmails;  // тут емейлы всех зарегистрированных пользователей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        startUploadUsers(); // сразу же начинаем выкачивать все емейлы пользователей
        init();
        scrollerBack();
        changeFonts(); // изменение шрифтов
    }

    private void startUploadUsers() {
        UploadAllUsersFromServer uaufs = new UploadAllUsersFromServer(this);
        uaufs.uploadUsers();
    } // upload all user from server. We will take result in emails method, that located bottom

    private void scrollerBack() {
        scroll_reg.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                image_back_registration.setY(-(scroll_reg.getScrollY() / 20));
            }
        });
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_text_reg));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.enter_your_data_reg));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_toolbar));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_et_email));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_et_password));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_et_password_confirm));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_et_name));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_et_number_of_phone));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_cb_driver));
        FontsDriver.changeFontToComfort(this, (TextView) findViewById(R.id.a_cb_companion));
        FontsDriver.changeFontToComfort(this, (Button) findViewById(R.id.a_b_registration));
    } // изменяем шрифты вьюх

    private void init() {

        a_et_email = (EditText) findViewById(R.id.a_et_email);
        a_et_password = (EditText) findViewById(R.id.a_et_password);
        a_et_password_confirm = (EditText) findViewById(R.id.a_et_password_confirm);
        a_et_name = (EditText) findViewById(R.id.a_et_name);
        a_et_number_of_phone = (EditText) findViewById(R.id.a_et_number_of_phone);

        a_cb_driver = (RadioButton) findViewById(R.id.a_cb_driver);
        a_cb_companion = (RadioButton) findViewById(R.id.a_cb_companion);
        //(findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);

        scroll_reg = (ScrollView) findViewById(R.id.scroll_reg);
        image_back_registration = (ImageView) findViewById(R.id.image_back_registration);
    }

    public void click_buttons(View view) {
        switch (view.getId()) {
            case R.id.a_b_registration:
                registration();
                break;
            default:
                break;
        }
    }

    private void registration() {
        if (a_cb_driver.isChecked()) {
            if (!a_et_email.getText().toString().equals("") &&
                    !a_et_password.getText().toString().equals("")
                    && a_et_password.getText().toString().equals(a_et_password_confirm.getText()
                    .toString()) && !a_et_name.getText().toString().equals("")) {
                (findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
                driver = new Driver("Номер банковской карты", "Обо мне", new Date().getTime() + "", 25,
                        "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png", "0",
                        a_et_email.getText().toString(), a_et_password.getText().toString()
                        , a_et_name.getText().toString(), "Авто", "1", a_et_number_of_phone
                        .getText().toString(), new ArrayList<Travel>(), new ArrayList<Travel>(),
                        new ArrayList<Travel>());
                if (!checkSingleUser()){
                    toast();
                }else{
                    Toast.makeText(this, "Вы уже зарегистрированы", Toast.LENGTH_SHORT).show();
                    (findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                }
                /**
                 * тут надо проверять есть ли такой юзер или нет
                 * */
            }else{
                Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
            }
        } else if (a_cb_companion.isChecked()) {
            if (!a_et_email.getText().toString().equals("") &&
                    !a_et_password.getText().toString().equals("")
                    && a_et_password.getText().toString().equals(a_et_password_confirm.getText()
                    .toString())) {
                (findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
                companion = new Companion("Обо мне", new Date().getTime() + ""
                        , 18, "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png"
                        , a_et_email.getText().toString(), a_et_password.getText().toString()
                        , a_et_name.getText().toString(), a_et_number_of_phone.getText().toString());
                if (!checkSingleUser()){
                    toast();
                }else{
                    Toast.makeText(this, "Вы уже зарегистрированы", Toast.LENGTH_SHORT).show();
                    (findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                }
                /**
                 * тут надо проверять есть ли такой юзер или нет
                * */
            }else{
                Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Выберите тип аккаунта и введите свои данные", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkSingleUser() {
        boolean b = false;
        if (driver!=null){
            for (String s : usersEmails){
                if (driver.getEmail().equals(s)){
                    b=true;
                }
            }
        }else if (companion!=null){
            for (String s : usersEmails){
                if (companion.getEmail().equals(s)){
                    b=true;
                }
            }
        }
        return b;
    } // this method check, have or not have list with usersemail (from firebase) email user, that doing registration now.

    private void saveToFirebaseCompanion() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("companion")
                .child(companion.getDate_create() + "")
                .setValue(companion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Data data = new Data(Registration.this);
                data.saveSharedPreferenceCOMPANION(companion);

                if (b){
                    Intent intent = new Intent(Registration.this, MainListActivity.class);
                    intent.putExtra("companion", companion);
                    Toast.makeText(Registration.this, "Вошел компаньон", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }


            }
        });
    }

    private void saveToFirebaseDriver() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("drivers")
                .child(driver.getDate_create() + "")
                .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Data data = new Data(Registration.this);
                data.saveSharedPreferenceDRIVER(driver);

                if (b){
                    Intent intent = new Intent(Registration.this, MainListActivity.class);
                    Toast.makeText(Registration.this, "Вошел драйвер", Toast.LENGTH_SHORT).show();
                    intent.putExtra("driver", driver);
                    startActivity(intent);
                }
            }
        });
    }

    private void toast() {
        new AlertDialog.Builder(this).setTitle("Правила пользования сервисом").setMessage(R.string.rules).setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b = true;
                dialog.dismiss();
                if (a_cb_companion.isChecked()){
                    saveToFirebaseCompanion();
                }else if (a_cb_driver.isChecked()){
                    saveToFirebaseDriver();
                }
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b = false;
                dialog.dismiss();
                finish();
            }
        }).create().show();
    }

    @Override
    public void emails(List<String> emails) {
        // in this method we can hint progress bar, when user only enter in this activity
        usersEmails = emails;
        System.out.println("email = " + a_et_email.getText().toString());
        for (String s : emails){
            System.out.println("s = " + s);
        }
        (findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
        findViewById(R.id.a_b_registration).setClickable(true); // enabled click button Registration
    } // here we take emails all users from server
}
