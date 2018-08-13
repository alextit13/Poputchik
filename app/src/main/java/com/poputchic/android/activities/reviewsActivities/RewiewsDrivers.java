package com.poputchic.android.activities.reviewsActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

import java.util.ArrayList;
import java.util.Date;

public class RewiewsDrivers extends Activity {

    private ArrayList<String> list = new ArrayList<String>();
    private Companion companion_I,companion_about;
    private Driver driver_I,driver_about;
    private ListView listView;
    private Button buttonAdd;
    private EditText add_rev_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewiews_drivers);
        initViews(); // инициализируем поля
        changeFonts(); // изменяем шрифты во вьюхах
        takeDataFromIntent(getIntent()); // забираем данные из интента
        hintButtonAndEditText();
        checkDriverOrCompanion(); // кто вошел драйвер или попутчик
    }

    private void changeFonts() {
        /*FontsConteroller.changeFontToComfort(this,buttonAdd);
        FontsConteroller.changeFontToComfort(this,add_rev_edit_text);*/
    } // изменяем шрифты во вьюхах

    private void initViews() {
        add_rev_edit_text = (EditText) findViewById(R.id.add_rev_edit_text);
        listView = (ListView) findViewById(R.id.list_reviews_drivers);
        buttonAdd = (Button) findViewById(R.id.btn_add_rev);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRev();
            }
        });
    } // инициализируем поля

    private void takeDataFromIntent(Intent intent) {
        try {
            companion_about = (Companion) getIntent().getSerializableExtra("companion");
            companion_I = (Companion) getIntent().getSerializableExtra("companion_I");
            driver_about = (Driver) getIntent().getSerializableExtra("driver");
            driver_I = (Driver) getIntent().getSerializableExtra("driver_I");
        }catch (Exception e){
            // Log...
        }
    } // забираем данные из интента

    private void hintButtonAndEditText() {
        if (companion_about!=null&&companion_I!=null){
            if (companion_about.getEmail().equals(companion_I.getEmail())){
                buttonAdd.setVisibility(View.INVISIBLE);
                add_rev_edit_text.setVisibility(View.INVISIBLE);
            }
        }
        if (driver_about!=null&&driver_I!=null){
            if (driver_I.getEmail().equals(driver_about.getEmail())){
                buttonAdd.setVisibility(View.INVISIBLE);
                add_rev_edit_text.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void checkDriverOrCompanion(){
        if (companion_about!=null){
            takeDataCompanion(companion_about);
        }else if (driver_about!=null){
            takeDataDriver(driver_about);
        }
    } // кто вошел драйвер или компаньон

    private void addRev() {
        if (driver_about!=null&&!add_rev_edit_text.getText().toString().equals("")) {
            FirebaseDatabase.getInstance().getReference().child("reviews")
                    .child(driver_about.getDate_create() + "")
                    .child(new Date().getTime() + "")
                    .setValue(add_rev_edit_text.getText().toString());
            add_rev_edit_text.setText("");
        }else if (companion_about!=null){
            FirebaseDatabase.getInstance().getReference().child("reviews")
                    .child(companion_about.getDate_create() + "")
                    .child(new Date().getTime() + "")
                    .setValue(add_rev_edit_text.getText().toString());
            add_rev_edit_text.setText("");
        }
    }

    private void takeDataDriver(Driver driver) {
        FirebaseDatabase.getInstance().getReference().child("reviews").child(driver.getDate_create()+"")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            list.add(data.getValue(String.class));
                        }
                        adapter(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void adapter(ArrayList<String> list) {
        ArrayAdapter adapter = new ArrayAdapter<String>(RewiewsDrivers.this,android.R.layout.simple_spinner_item,list);
        listView.setAdapter(adapter);
    }

    private void takeDataCompanion(Companion companion) {
        FirebaseDatabase.getInstance().getReference().child("reviews").child(companion.getDate_create()+"")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            list.add(data.getValue(String.class));
                        }
                        adapter(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
