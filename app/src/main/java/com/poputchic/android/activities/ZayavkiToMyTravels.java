package com.poputchic.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.adapters.ZayavkaAdapter;
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Zayavka;
import com.poputchic.android.models.ZayavkaFromCompanion;

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
        changeFonts();
    }

    private void changeFonts() {
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.activity_zayavki_to_my_travels_logo));
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.logo_my_travels));
    }

    private void takeList() {
        list_zayavka = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("zayavki").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_zayavka.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if (data.getValue(Zayavka.class).getDriver().equals(driver.getDate_create())){
                        list_zayavka.add(data.getValue(Zayavka.class));
                    }
                }
                Log.d(VARIABLES_CLASS.LOG_TAG,"size list = " + list_zayavka.size());

                /***
                 *
                 * goToadapter(list_zayavka);
                 * **/
                takeAnotherZayavki(list_zayavka);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void takeAnotherZayavki(final ArrayList<Zayavka> LZ) {
        final ArrayList<ZayavkaFromCompanion>list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    list.add(data.getValue(ZayavkaFromCompanion.class));
                                }
                                Log.d(VARIABLES_CLASS.LOG_TAG,"listSize = " + list.size());
                                sortArray(list,LZ);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    private void sortArray(ArrayList<ZayavkaFromCompanion> list, ArrayList<Zayavka> lz) {

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
