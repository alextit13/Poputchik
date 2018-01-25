package com.poputchic.android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.poputchic.android.R;
import com.poputchic.android.adapters.CompanionAdapter;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.reg_and_sign.SignInOrRegistration;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity {

    private ListView b_main_list;
    private ImageView b_menu_1,b_menu_2,b_menu_3,b_menu_4,b_menu_5;
    private Driver driver;
    private Companion companion;

    private ArrayList<Companion>listCompanions;
    private ArrayList<Travel> listTravesl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        init();
    }

    private void init() {
        b_main_list = (ListView)findViewById(R.id.b_main_list);
        b_menu_1 = (ImageView) findViewById(R.id.b_menu_1);
        b_menu_2 = (ImageView) findViewById(R.id.b_menu_2);
        b_menu_3 = (ImageView) findViewById(R.id.b_menu_3);
        b_menu_4 = (ImageView) findViewById(R.id.b_menu_4);
        b_menu_5 = (ImageView) findViewById(R.id.b_menu_5);
        selectUser();
    }

    private void selectUser() {
        Intent intent = getIntent();
        try {
            driver = (Driver) intent.getSerializableExtra("driver");
            companion = (Companion) intent.getSerializableExtra("companion");
        }catch (Exception e){
            //Log...
        }
        if (driver!=null){
            // вошел водитель
            takeAndStartWithListTravels();
        }else if (companion!=null){
            // вошел пользователь
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
                        travelsAdapter(listTravesl);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void travelsAdapter(ArrayList<Travel> LDR) {
        TravelAdapter adapter = new TravelAdapter(this,LDR);
        b_main_list.setAdapter(adapter);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.b_menu_1:
                // private room
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
                // ?
                break;
            case R.id.b_menu_5:
                // ?
                break;

        }
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
                        try {
                            // отрываем поток для записи
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                    openFileOutput("FILENAME", MODE_PRIVATE)));
                            // пишем данные
                            Gson gson = new Gson();
                            String json = gson.toJson(null);


                            bw.write(json);
                            // закрываем поток
                            bw.close();
                            //Log.d(MainActivity.LOG_TAG, "Файл записан");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MainListActivity.this, SignInOrRegistration.class);
                        startActivity(intent);
                        dialog.dismiss();
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
