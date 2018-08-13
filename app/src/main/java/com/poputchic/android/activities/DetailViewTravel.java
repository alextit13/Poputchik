package com.poputchic.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Travel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailViewTravel extends Activity {

    private Companion companion;
    private Travel travel;
    private ListView list_review_about_driver;
    private TextView text_driver;
    private ImageView face_driver,back_button_my_travels;
    private Driver driver;
    private List<String>listReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_travel);

        init();
        changeFont();
        completeViews();
    }

    private void changeFont() {
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.activity_detail_view_travel_logo));
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.reviews_about_users));
    }

    private void completeViews() {
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(travel.getDriver_create())
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                driver = dataSnapshot.getValue(Driver.class);
                                goTocomplete(driver);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    private void goTocomplete(Driver D) {
        text_driver.setText(D.getName()+"");
        Picasso.with(DetailViewTravel.this).load(D.getImage_path()).into(face_driver);

        FirebaseDatabase.getInstance().getReference().child("reviews").child(driver.getDate_create()+"")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    listReviews.add(data.getValue(String.class));
                                }
                                toAdapter(listReviews);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

    }

    private void toAdapter(List<String> LR) {
        ArrayAdapter adapter = new ArrayAdapter<String>(DetailViewTravel.this,android.R.layout.simple_list_item_1,
                LR);
        list_review_about_driver.setAdapter(adapter);

    }

    private void init() {
        face_driver = (ImageView) findViewById(R.id.face_driver);
        (findViewById(R.id.back_button_my_travels)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        companion = (Companion) getIntent().getSerializableExtra("companion");
        travel = (Travel) getIntent().getSerializableExtra("travel");
        list_review_about_driver = (ListView) findViewById(R.id.list_review_about_driver);
        text_driver = (TextView) findViewById(R.id.text_driver);
    }
}
