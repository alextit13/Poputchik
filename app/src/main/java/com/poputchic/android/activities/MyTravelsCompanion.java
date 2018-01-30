package com.poputchic.android.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.Zayavka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTravelsCompanion extends Activity {

    private ImageView back_button_my_travels;
    private ListView list_my_z;
    private ArrayList<String>listStrings = new ArrayList<>();
    private ArrayList<Travel> listMyZ = new ArrayList<>();
    private Object list;
    private Companion companion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels_companion);
        init();
    }

    private void init() {
        companion = (Companion) getIntent().getSerializableExtra("companion");
        back_button_my_travels = (ImageView) findViewById(R.id.back_button_my_travels);
        list_my_z = (ListView) findViewById(R.id.list_my_z);
        getList();
    }

    public void getList() {
        FirebaseDatabase.getInstance().getReference().child("travels_complete_companion").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot data : dataSnapshot.getChildren()){

                            FirebaseDatabase.getInstance().getReference().child("travels_complete_companion").child(data.getKey()+"").addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                                if (d.getValue(String.class).equals(companion.getDate_create()+"")){
                                                    listStrings.add(data.getKey()+"");
                                                    Log.d(VARIABLES_CLASS.LOG_TAG,"LS = " + data.getKey());
                                                    getZayavkiFromFirebase(listStrings);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getZayavkiFromFirebase(ArrayList<String> LS) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"size = " + LS.size());
        if (!LS.isEmpty()){
            for (int i = 0; i<LS.size();i++){
                FirebaseDatabase.getInstance().getReference().child("travels").child(LS.get(i)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                listMyZ.add(dataSnapshot.getValue(Travel.class));
                                goToAdapter();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }
        }
    }

    private void goToAdapter() {
        if (!listMyZ.isEmpty()){
            TravelAdapter adapter = new TravelAdapter(MyTravelsCompanion.this,listMyZ,companion,1);
            list_my_z.setAdapter(adapter);
            list_my_z.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }
    }
}
