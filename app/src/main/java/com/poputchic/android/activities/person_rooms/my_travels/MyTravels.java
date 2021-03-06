package com.poputchic.android.activities.person_rooms.my_travels;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.Zayavka;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Travel;

import java.util.ArrayList;

public class MyTravels extends Activity implements TravelAdapter.RefreshAdapter {

    private ImageView back_button_my_travels;
    private ListView list_my_travels;
    private ArrayList<Travel>list_trawels;
    private Driver driver;
    private int num = 0;
    private TravelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels);
        init();
        changeFonts(); // изменение шрифта вьюх
        takeDriver();
        clicker();
        completeList();
    }

    private void changeFonts() {
        /*FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.activity_my_travels_label));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.logo_my_travels));*/
    } // изменяем шрифты вьюх

    private void takeDriver() {
        driver = (Driver) getIntent().getSerializableExtra("driver");
    }

    private void completeList() {
        list_trawels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("travels").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if ((data.getValue(Travel.class).getDriver_create()+"")
                            .equals(driver.getDate_create()+"")){
                        list_trawels.add(data.getValue(Travel.class));
                    }
                }
                if (adapter == null)
                adapter = new TravelAdapter(MyTravels.this,list_trawels,null,MyTravels.this);
                list_my_travels.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void clicker() {
        back_button_my_travels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_my_travels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MyTravels.this).setTitle("Удаление")
                        .setMessage("Вы действительно хотите удалить поездку?")
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //Log.d(VARIABLES_CLASS.LOG_TAG,"i = " + i);
                        num = position;
                        deleteItemTravel();
                        finish();
                    }
                }).create().show();
                return true;
            }
        });
    }

    private void deleteItemTravel() {
        //Log.d(VARIABLES_CLASS.LOG_TAG,"num = " + num);
        FirebaseDatabase.getInstance().getReference().child("travels").child(list_trawels.get(num)
        .getTime_create()+"").removeValue();

        deleteAllZayavkiForThisTravel(list_trawels.get(num).getTime_create()+"");

    }

    private void deleteAllZayavkiForThisTravel(final String s) {
        FirebaseDatabase.getInstance().getReference().child("zayavki").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String travel = "";
                    try {
                        travel = data.getValue(Zayavka.class).getTravel();
                    }catch (NullPointerException e){e.printStackTrace();}

                    if (travel.equals(s)){
                        FirebaseDatabase.getInstance().getReference().child("zayavki").child(data.getValue(Zayavka.class).getDateCreate())
                                .removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        back_button_my_travels = (ImageView) findViewById(R.id.back_button_my_travels);
        list_my_travels = (ListView) findViewById(R.id.list_my_travels);

    }

    @Override
    public void refresh() {
//        adapter = new TravelAdapter(MyTravels.this,list_trawels,null,this);
//        list_my_travels.setAdapter(adapter);
        completeList();
    }
}
