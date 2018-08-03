package com.poputchic.android.activities.reg_and_sign;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UploadAllUsersFromServer {
    /** this class take all users from server and get all emails from users
     * and put them to list */

    private Activity activity;
    private List<String>listEmails = new LinkedList<>();

    public UploadAllUsersFromServer(Activity activity) {
        this.activity = activity;
    }

    public void uploadUsers() {
        // first get emails in drivers
        getDriversEmails();
    } // take all emails

    private void getDriversEmails(){
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listEmails.add(data.getValue(Driver.class).getEmail());
                        }
                        getCompanionEmails(); // if upload is finish - start upload all companions
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }// get all drivers emails

    private void getCompanionEmails(){
        FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listEmails.add(data.getValue(Companion.class).getEmail());
                        }
                        // uploads all users is finish
                        AllUsersFromServer aufs = (AllUsersFromServer)activity;
                        aufs.emails(listEmails);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    } // get all companions emails

    interface AllUsersFromServer{
        void emails(List<String>emails);
    } // интерфейс пользователей из сервера
}
