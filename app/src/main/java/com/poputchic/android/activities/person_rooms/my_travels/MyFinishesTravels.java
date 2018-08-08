package com.poputchic.android.activities.person_rooms.my_travels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.adapters.LZFC.LZFCAdapter;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;

import java.util.ArrayList;
import java.util.List;

public class MyFinishesTravels extends Activity {

    private ImageView pr_iv_my_travels_finish_c;
    private ListView companion_finish_travels;
    private Companion companion;
    private List<ZayavkaFromCompanion>list = new ArrayList<>();
    private List<Companion>listCompanions = new ArrayList<>();
    private List<Driver>listDrivers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_finishes_travels);

        init();
        clisker();
        takeDataFromServer();
    }

    private void takeDataFromServer() {
        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            if (data.getValue(ZayavkaFromCompanion.class).getCompanion().equals(companion.getDate_create())){
                                list.add(data.getValue(ZayavkaFromCompanion.class));
                            }
                        }
                        getCompanions();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getCompanions() {
        if (list.size()>0){
            for (int i = 0; i<list.size();i++){
                FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(list.get(i).getCompanion()+"")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        listCompanions.add(dataSnapshot.getValue(Companion.class));
                                        if (listCompanions.size()==list.size()){
                                            getDriversFromZayavki();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
            }
        }else{
            Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDriversFromZayavki() {
        for (int i = 0; i<list.size();i++){
            FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(list.get(i).getDriver()+"")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    listDrivers.add(dataSnapshot.getValue(Driver.class));
                                    if (listDrivers.size()==list.size()){
                                        adapter();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );
        }
    }

    private void adapter() {
        LZFCAdapter adapter = new LZFCAdapter(this,list,null,listCompanions,listDrivers);
        companion_finish_travels.setAdapter(adapter);
    }

    private void clisker() {
        pr_iv_my_travels_finish_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        companion = (Companion) getIntent().getSerializableExtra("companion");
        pr_iv_my_travels_finish_c = (ImageView) findViewById(R.id.pr_iv_my_travels_finish_c);
        companion_finish_travels = (ListView) findViewById(R.id.companion_finish_travels);
        changeFonts();// изменяем шрифт вьюх
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.activity_my_finishes_travel_label));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.logo_my_travels));
    }
}
