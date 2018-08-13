package com.poputchic.android.presenters.registrationPresenter;

import android.widget.EditText;

import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

import java.util.List;

public interface IRegistrationPresenter {
    void downloadAllDrivers();
    void downloadAllCompanions();
    boolean checkUserCompleteFields(String email, String p_1, String p_2, String name);
    boolean checkSingleUser(List<String> emailsUsers, String email);
    Driver createDriver(List<EditText>listFields);
    Companion createCompanion(List<EditText>listFields);
    void saveToFirebaseCompanion(Companion companion);
    void saveToFirebaseDriver(Driver driver);
}
