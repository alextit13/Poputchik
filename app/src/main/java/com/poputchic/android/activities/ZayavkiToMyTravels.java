package com.poputchic.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.adapters.ZayavkaAdapter;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Zayavka;

import java.util.ArrayList;

public class ZayavkiToMyTravels extends Activity {

    private ListView list_my_travels;
    private ArrayList <Zayavka> list_zayavka;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zayavki_to_my_travels);

        init();
        takeList();
    }

    private void takeList() {
        list_zayavka = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("zayavki").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if (data.getValue(Zayavka.class).getDriver().equals(driver.getDate_create())){
                        list_zayavka.add(data.getValue(Zayavka.class));
                    }
                }
                goToadapter(list_zayavka);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToadapter(ArrayList<Zayavka> LZ) {
        ZayavkaAdapter adapter = new ZayavkaAdapter(this,LZ,driver);
        list_my_travels.setAdapter(adapter);
    }

    private void init() {
        list_my_travels = (ListView) findViewById(R.id.list_my_travels);
        driver = (Driver) getIntent().getSerializableExtra("driver");
    }
}
