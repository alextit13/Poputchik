package com.poputchic.android.models.driver;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.presenters.registrationPresenter.RegistrationPresenter;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDriverList implements IFirebaseDriverList{

    private RegistrationPresenter presenter;
    private List<String> listEmails = new ArrayList<>();

    public FirebaseDriverList(RegistrationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void downloadAllDrivers() {
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            listEmails.add(data.getValue(Driver.class).getEmail());
                        }
                        ResultListEmailsDrivers resultListEmailsDrivers = (ResultListEmailsDrivers) presenter;
                        resultListEmailsDrivers.EmailsDriverResultList(listEmails);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // get all drivers emails
    }
    public interface ResultListEmailsDrivers{
        void EmailsDriverResultList(List<String>emails);
    }
}
