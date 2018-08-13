package com.poputchic.android.models.companion;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.presenters.registrationPresenter.RegistrationPresenter;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCompanionList implements IFirebaseCompanionList{

    /** класс для получения емейлов всех компаньонов */

    private List<String>listEmails = new ArrayList<>();
    private RegistrationPresenter presenter;

    public FirebaseCompanionList(RegistrationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void downloadCompanionList() {
        FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listEmails.add(data.getValue(Companion.class).getEmail());
                        }
                        ResultListEmailsCompanions resultListEmailsCompanions = (ResultListEmailsCompanions) presenter;
                        resultListEmailsCompanions.EmailsCommpanionResultList(listEmails);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public interface ResultListEmailsCompanions{
        void EmailsCommpanionResultList(List<String>emails);
    }



}
