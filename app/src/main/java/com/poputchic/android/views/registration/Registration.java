package com.poputchic.android.views.registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.R;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.models.Data;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.presenters.registrationPresenter.RegistrationPresenter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Registration extends Activity implements IRegistration, RegistrationPresenter.ResultDownloadEmailsList {

    private Companion companion;
    private Driver driver;
    private List<String> usersEmails;  // тут емейлы всех зарегистрированных пользователей

    private RegistrationPresenter presenter;
    @BindViews({R.id.a_et_email, R.id.a_et_password, R.id.a_et_password_confirm, R.id.a_et_name, R.id.a_et_number_of_phone})
    List<EditText> listEditTexts;
    @BindViews({R.id.a_cb_driver, R.id.a_cb_companion}) List<RadioButton> listRadioButtons;
    @BindView(R.id.scroll_reg) ScrollView scroll_reg;
    @BindView(R.id.image_back_registration) ImageView image_back_registration;
    @BindView(R.id.a_pb) AVLoadingIndicatorView progress;
    @BindView(R.id.a_b_registration) Button buttonRegistration;
    @BindViews({R.id.a_text_reg, R.id.enter_your_data_reg,
            R.id.a_toolbar, R.id.a_et_email, R.id.a_et_password, R.id.a_et_password_confirm, R.id.a_et_name,
            R.id.a_et_number_of_phone, R.id.a_cb_driver, R.id.a_cb_companion})
    List<TextView> listTextViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        presenter = new RegistrationPresenter(this);

        startUploadUsers(); // сразу же начинаем выкачивать все емейлы пользователей

        scrollerBack();
        changeFonts(); // изменение шрифтов
    }

    private void startUploadUsers() {
        RegistrationPresenter presenter = new RegistrationPresenter(this);
        presenter.downloadAllDrivers();
    } // upload all user from server. We will take result in emails method, that located bottom

    private void scrollerBack() {
        AnimationBackground animationBackground = new AnimationBackground();
        animationBackground.animation(scroll_reg, image_back_registration);
    }

    private void changeFonts() {
        FontsConteroller fontsConteroller = new FontsConteroller(this);
        fontsConteroller.changeAllListFontsInViews(listEditTexts);
        fontsConteroller.changeAllListFontsInViewsTV(listTextViews);
        fontsConteroller.changeFontToComfortButton(buttonRegistration);
    } // изменяем шрифты вьюх

    public void click_buttons_registration(View view) {
        registration();
    }

    private void registration() {
        if (listRadioButtons.get(0).isChecked()) {
            if (presenter.checkUserCompleteFields(listEditTexts.get(0).getText().toString(), listEditTexts.get(1).getText().toString(),
                    listEditTexts.get(2).getText().toString(), listEditTexts.get(3).getText().toString())) {
                driver = presenter.createDriver(listEditTexts);
                if (presenter.checkSingleUser(usersEmails,listEditTexts.get(0).getText().toString())) {
                    toast();
                } else {
                    Toast.makeText(this, "Вы уже зарегистрированы", Toast.LENGTH_SHORT).show();
                    (findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
            }

        } else if (listRadioButtons.get(1).isChecked()) {
            if (presenter.checkUserCompleteFields(listEditTexts.get(0).getText().toString(), listEditTexts.get(1).getText().toString(),
                    listEditTexts.get(2).getText().toString(), listEditTexts.get(3).getText().toString())) {
                companion = presenter.createCompanion(listEditTexts);
                if (presenter.checkSingleUser(usersEmails,listEditTexts.get(0).getText().toString())) {
                    toast();
                } else {
                    Toast.makeText(this, "Вы уже зарегистрированы", Toast.LENGTH_SHORT).show();
                    (findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toast() {
        ToastRules toastRules = new ToastRules();
        toastRules.ToastRulesGeneration(this, presenter,companion,driver);
    }

    @Override
    public void resultListEmailsAllUsers(List<String> listEmails) {
        usersEmails = listEmails;
        progress.setVisibility(View.GONE);
        buttonRegistration.setClickable(true);
    }
}
