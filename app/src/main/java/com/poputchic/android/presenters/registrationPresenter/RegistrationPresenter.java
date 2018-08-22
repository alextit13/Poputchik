package com.poputchic.android.presenters.registrationPresenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.models.Data;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.companion.FirebaseCompanionList;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.driver.FirebaseDriverList;
import com.poputchic.android.views.registration.Registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RegistrationPresenter implements IRegistrationPresenter,FirebaseDriverList.ResultListEmailsDrivers
        ,FirebaseCompanionList.ResultListEmailsCompanions {

    private Activity activity;
    private List<String> listEmails = new LinkedList<>();
    private FirebaseDriverList firebaseDriverList; // model

    public RegistrationPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void downloadAllDrivers() {
        firebaseDriverList = new FirebaseDriverList(this);
        firebaseDriverList.downloadAllDrivers();
    }

    @Override
    public void EmailsDriverResultList(List<String> emails) {
        listEmails.addAll(emails);
        downloadAllCompanions();
    }

    @Override
    public void downloadAllCompanions() {
        FirebaseCompanionList firebaseCompanionList = new FirebaseCompanionList(this);
        firebaseCompanionList.downloadCompanionList();
    }

    @Override
    public boolean checkUserCompleteFields(String email, String p_1, String p_2, String name) {
        return new Driver().checkDriverCompleteFields(email,p_1,p_2,name);
    }

    @Override
    public boolean checkSingleUser(List<String> emailsUsers, String email) {
        return new Driver().checkSingleDriver(emailsUsers,email);
    }

    @Override
    public Driver createDriver(List<EditText> listFields) {
        return new Driver("Номер банковской карты", "Обо мне", new Date().getTime() + "", 25,
                "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png", "0",
                listFields.get(0).getText().toString(), listFields.get(1).getText().toString()
                , listFields.get(3).getText().toString(), "Авто", "1",
                listFields.get(4).getText().toString(), new ArrayList<Travel>(), new ArrayList<Travel>(),
                new ArrayList<Travel>(),listFields.get(5).getText().toString(),listFields.get(6).getText().toString());
    }

    @Override
    public Companion createCompanion(List<EditText> listFields) {
        return new Companion("Обо мне", new Date().getTime() + ""
                , 18, "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png"
                , listFields.get(0).getText().toString(), listFields.get(1).getText().toString()
                , listFields.get(3).getText().toString(), listFields.get(4).getText().toString()
                , listFields.get(5).getText().toString(),listFields.get(6).getText().toString(),0);
    }

    @Override
    public void saveToFirebaseCompanion(final Companion companion) {
        companion.registrationCompanion(activity,companion);
    }

    @Override
    public void saveToFirebaseDriver(Driver driver) {
        driver.saveDriverToFirebase(activity, driver);
    }

    @Override
    public void EmailsCommpanionResultList(List<String> emails) {
        listEmails.addAll(emails);
        ResultDownloadEmailsList resultDownloadEmailsList = (ResultDownloadEmailsList)activity;
        resultDownloadEmailsList.resultListEmailsAllUsers(listEmails);
    }

    public interface ResultDownloadEmailsList{
        void resultListEmailsAllUsers(List<String>listEmails);
    }

}
