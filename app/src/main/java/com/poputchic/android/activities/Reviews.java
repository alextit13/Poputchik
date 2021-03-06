package com.poputchic.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.activities.reviewsActivities.RewiewsDrivers;
import com.poputchic.android.adapters.CompanionAdapter;
import com.poputchic.android.adapters.DriversAdapter;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

import java.util.ArrayList;

public class Reviews extends Activity {

    //private Companion companion;
    private Driver driver;
    private Companion companion;
    private ListView list;
    private ArrayList<Companion>listCompanions = new ArrayList<>();
    private ArrayList<Driver>listDrivers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        init();
        changeFonts();
    }

    private void changeFonts() {
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.title_activity_reviews));
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                controller.myProfile(driver,companion);
                break;
            case R.id.b_menu_2:
                //controller.exit();
                finish();
                break;
            case R.id.b_menu_3:
                controller.addClick(driver,companion);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(driver,companion);
                break;
            case R.id.b_menu_5:
                // ?
                //controller.usersList(driver,companion);
                break;
        }
    }

    private void init() {
        list = (ListView) findViewById(R.id.list_review);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click(position);
            }
        });
        Intent intent = getIntent();
        try {
            driver = (Driver) intent.getSerializableExtra("driver");
            companion = (Companion) intent.getSerializableExtra("companion");
        }catch (Exception e){
            //Log...
        }

        if (driver!=null){
            getListCompanions(driver);
        }else if (companion!=null){
            getListDrivers(companion);
        }
    }

    private void click(final int p) {
        if (companion!=null){
            getListCompanions(null);
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child("companion").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listCompanions.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                listCompanions.add(data.getValue(Companion.class));
                            }
                            Intent intent = new Intent(Reviews.this, RewiewsDrivers.class);
                            intent.putExtra("companion", listCompanions.get(p));
                            intent.putExtra("companion_I", companion);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }else if (driver!=null){
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child("drivers").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listDrivers.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                listDrivers.add(data.getValue(Driver.class));
                            }
                            Intent intent = new Intent(Reviews.this, RewiewsDrivers.class);
                            intent.putExtra("driver", listDrivers.get(p));
                            intent.putExtra("driver_I", driver);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
    }

    private void getListCompanions(final Driver D) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("companion").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listCompanions.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listCompanions.add(data.getValue(Companion.class));
                        }
                        if (D!=null){
                            goToAdapterCompanions(listCompanions);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getListDrivers(final Companion c) {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("drivers").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listDrivers.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listDrivers.add(data.getValue(Driver.class));
                        }
                        if (c != null) {
                            goToAdapterDrivers(listDrivers);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void goToAdapterCompanions(ArrayList<Companion> C) {
        CompanionAdapter adapter = new CompanionAdapter(Reviews.this,C);
        list.setAdapter(adapter);
    }

    private void goToAdapterDrivers(ArrayList<Driver>L) {
        DriversAdapter adapter = new DriversAdapter(Reviews.this,L);
        list.setAdapter(adapter);
    }
}
