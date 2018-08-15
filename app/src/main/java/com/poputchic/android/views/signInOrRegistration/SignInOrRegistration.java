package com.poputchic.android.views.signInOrRegistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poputchic.android.activities.reg_and_sign.SignIn;
import com.poputchic.android.R;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.presenters.sigmInOrRegistrationPresenter.SignInOrRegistrationPresenter;
import com.poputchic.android.views.registration.Registration;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class SignInOrRegistration extends Activity implements SignInOrRegistrationPresenter.StarterInterfaceSelect {


    @BindViews({R.id.c_b_sign_in,R.id.c_b_registration}) List<Button> listButtons;
    @BindView(R.id.c_toolbar) TextView tv;
    private SignInOrRegistrationPresenter presenter;

    //private ProgressBar progress_bar_sign_or_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_registration);
        ButterKnife.bind(this);
        presenter = new SignInOrRegistrationPresenter(this);
        changeFonts(); // изменяем шрифт на вьюхах
    }

    private void changeFonts() {
        FontsConteroller fontsConteroller = new FontsConteroller(this);
        fontsConteroller.changeFontToComfortButton(listButtons);
        fontsConteroller.changeFontToComfort(tv);
    } // change viws on views

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.c_b_sign_in:
                presenter.signIn();
                break;
            case R.id.c_b_registration:
                presenter.registrationIn();
                break;
            default:
                break;
        }
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

    @Override
    public void result(Intent intent) {
        startActivity(intent);
    }
}
