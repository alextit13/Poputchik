package com.poputchic.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.adapters.RewiewAdapter;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Review;

import java.util.ArrayList;
import java.util.List;

public class Reviews extends AppCompatActivity {

    //private Companion companion;
    private Driver driver;
    private ListView list_review;
    private ArrayList<Companion>list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        init();
    }

    private void init() {
        list_review = (ListView) findViewById(R.id.list_review);
        Intent intent = getIntent();
        try {
            driver = (Driver) intent.getSerializableExtra("driver");
            //companion = (Companion) intent.getSerializableExtra("companion");
        }catch (Exception e){
            //Log...
        }

        getList();
    }

    private void getList() {
        if (driver!=null){
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child("companion").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                list.add(data.getValue(Companion.class));
                            }
                            goToAdapter(list);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
        /*if (companion!=null){
            FirebaseDatabase.getInstance().getReference().child("reviews")
                    .child("companions").child(companion.getDate_create()).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dat0a : dataSnapshot.getChildren()){
                                list.add(data.getValue(Review.class));
                            }
                            goToAdapter(list);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }*/
    }

    private void goToAdapter(ArrayList<Companion>list) {
        RewiewAdapter adapter = new RewiewAdapter();
        list_review.setAdapter(adapter);
    }
}
