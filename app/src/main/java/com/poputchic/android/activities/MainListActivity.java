package com.poputchic.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.person_rooms.PersonRoomCompanion;
import com.poputchic.android.activities.person_rooms.PersonRoomDriver;
import com.poputchic.android.activities.person_rooms.my_travels.MyTravels;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import java.util.ArrayList;
import java.util.HashSet;

public class MainListActivity extends Activity {

    private ListView b_main_list;
    private ImageView b_menu_1,b_menu_2,b_menu_3,b_menu_4,b_menu_5;
    private Driver driver;
    private Companion companion;

    private ArrayList<Companion>listCompanions;
    private ArrayList<Travel> listTravesl;

    private RelativeLayout main_list_container;
    private ProgressBar main_list_progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        init();
    }

    private void init() {
        main_list_progress_bar = (ProgressBar) findViewById(R.id.main_list_progress_bar);
        main_list_progress_bar.setVisibility(View.VISIBLE);
        b_main_list = (ListView)findViewById(R.id.b_main_list);
        b_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (companion!=null){
                    clickPosition(position);
                }
            }
        });
        b_menu_1 = (ImageView) findViewById(R.id.b_menu_1);
        b_menu_2 = (ImageView) findViewById(R.id.b_menu_2);
        b_menu_3 = (ImageView) findViewById(R.id.b_menu_3);
        b_menu_4 = (ImageView) findViewById(R.id.b_menu_4);
        b_menu_5 = (ImageView) findViewById(R.id.b_menu_5);
        selectUser();
    }

    private void clickPosition(int p) {
        Intent intent = new Intent(MainListActivity.this,DetailViewTravel.class);
        intent.putExtra("companion",companion);
        intent.putExtra("travel",listTravesl.get(p));
        startActivity(intent);
    }

    private void selectUser() {
        try {
            /*Data data = new Data(MainListActivity.this);
            driver = data.getDriverData();
            companion = data.getCompanionData();*/
            Intent intent = getIntent();

            driver = (Driver) intent.getSerializableExtra("driver");
            companion = (Companion) intent.getSerializableExtra("companion");
        }catch (Exception e){
            //Log...
        }
        Data data = new Data(MainListActivity.this);
        if (driver!=null){
            // вошел водитель
            data.saveSharedPreferenceDRIVER(driver);
            takeAndStartWithListTravels();
        }else if (companion!=null){
            // вошел пользователь
            data.saveSharedPreferenceCOMPANION(companion);
            takeAndStartWithListTravels();
        }
    }

    private void takeAndStartWithListTravels() {
        listTravesl = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("travels")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){

                            listTravesl.add(data.getValue(Travel.class));
                        }
                        listTravesl = deleteTwosItemsInMainList(listTravesl);
                        main_list_progress_bar.setVisibility(View.INVISIBLE);
                        travelsAdapter(listTravesl);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private ArrayList<Travel> deleteTwosItemsInMainList(ArrayList<Travel> LT) {
        HashSet<Travel> hashSet = new HashSet<Travel>();
        hashSet.addAll(LT);
        LT.clear();
        LT.addAll(hashSet);
        return LT;
    }

    private void travelsAdapter(ArrayList<Travel> LDR) {
        TravelAdapter adapter = new TravelAdapter(this,LDR,companion);
        b_main_list.setAdapter(adapter);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.b_menu_1:
                // private room
                if (driver!=null){
                    Toast.makeText(MainListActivity.this, "Личный кабинет драйвер", Toast.LENGTH_SHORT).show();
                    goToPrivateRoomDriver(driver);
                }else if (companion!=null){
                    Toast.makeText(MainListActivity.this, "Личный кабинет компаньон", Toast.LENGTH_SHORT).show();
                    goToPrivateRoomCompanion(companion);
                }
                break;
            case R.id.b_menu_2:
                // exit
                exit();
                break;
            case R.id.b_menu_3:
                // add
                if (driver!=null){
                    addTravel();
                }else if (companion!=null){
                    
                }
                break;
            case R.id.b_menu_4:
                if (driver!=null){
                    Intent intent = new Intent(MainListActivity.this, ZayavkiToMyTravels.class);
                    intent.putExtra("driver",driver);
                    startActivity(intent);
                }
                // ?
                break;
            case R.id.b_menu_5:
                // ?
                break;

        }
    }

    private void goToPrivateRoomCompanion(Companion CO) {
        Intent intent = new Intent(MainListActivity.this, PersonRoomCompanion.class);
        intent.putExtra("companion",CO);
        startActivity(intent);
    }

    private void goToPrivateRoomDriver(Driver DR) {
        Intent intent = new Intent(MainListActivity.this, PersonRoomDriver.class);
        intent.putExtra("driver",DR);
        startActivity(intent);
    }

    private void addTravel() {
        Intent intent = new Intent(MainListActivity.this,AddTravel.class);
        intent.putExtra("driver",driver);
        startActivity(intent);
    }

    private void exit() {
        new AlertDialog.Builder(MainListActivity.this)
                .setTitle("Выход")
                .setMessage("Вы действительно хотите выйти?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //the user wants to leave - so dismiss the dialog and exit
                        Data data = new Data(MainListActivity.this);
                        data.saveSharedPreferenceDRIVER(null);
                        data.saveSharedPreferenceCOMPANION(null);



                        finish();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .show();
    }
}
