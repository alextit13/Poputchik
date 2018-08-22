package com.poputchic.android.views.registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.RadioButton;
import android.widget.Toast;

import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.presenters.registrationPresenter.RegistrationPresenter;

public class ToastRules {

    boolean b = false;

    public void ToastRulesGeneration(final Activity activity, final RegistrationPresenter presenter, final Companion companion, final Driver driver){

        new AlertDialog.Builder(activity)
                .setTitle("Правила пользования сервисом")
                .setMessage(
                        activity.getResources().getString(R.string.rule_1) + "\n" +
                        activity.getResources().getString(R.string.rule_2 ) + "\n" +
                        activity.getResources().getString(R.string.rule_3 ) + "\n" +
                        activity.getResources().getString(R.string.rule_4 ) + "\n" +
                        activity.getResources().getString(R.string.rule_5 ) + "\n" +
                        activity.getResources().getString(R.string.rule_6 ) + "\n" +
                        activity.getResources().getString(R.string.rule_7 ) + "\n" +
                        activity.getResources().getString(R.string.rule_8 ) + "\n" +
                        activity.getResources().getString(R.string.rule_9 ) + "\n" +
                        activity.getResources().getString(R.string.rule_10) + "\n"
                )
                .setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b = true;
                        dialog.dismiss();
                        if (companion != null) {
                            presenter.saveToFirebaseCompanion(companion);
                    //*((Registration)activity).saveToFirebaseCompanion();*//*
                        } else if (driver != null) {
                            presenter.saveToFirebaseDriver(driver);
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b = false;
                        dialog.dismiss();
                        activity.finish();
                    }
                })
                .create().show();



        /*new AlertDialog.Builder(activity).setTitle("Правила пользования сервисом")
                .setMessage(R.string.rules).setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b = true;
                dialog.dismiss();
                if (companion != null) {
                    presenter.saveToFirebaseCompanion(companion);
                    *//*((Registration)activity).saveToFirebaseCompanion();*//*
                } else if (driver != null) {
                    presenter.saveToFirebaseDriver(driver);
                }
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b = false;
                dialog.dismiss();
                activity.finish();
            }
        }).create().show();*/
    }
}
