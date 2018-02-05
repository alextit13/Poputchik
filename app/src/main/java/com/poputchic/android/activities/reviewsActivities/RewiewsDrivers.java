package com.poputchic.android.activities.reviewsActivities;

import android.support.v7.app.AppCompatActivity;
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
import com.poputchic.android.R;
import com.poputchic.android.activities.Reviews;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

import java.util.ArrayList;
import java.util.Date;

public class RewiewsDrivers extends AppCompatActivity {

    private ArrayList <String> list = new ArrayList();
    private Companion companion;
    private Driver driver;
    private ListView listView;
    private Button buttonAdd;
    private EditText add_rev_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewiews_drivers);
        add_rev_edit_text = (EditText) findViewById(R.id.add_rev_edit_text);
        listView = (ListView) findViewById(R.id.list_reviews_drivers);
        buttonAdd = (Button) findViewById(R.id.btn_add_rev);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRev();
            }
        });

        try {
            companion = (Companion) getIntent().getSerializableExtra("companion");
            driver = (Driver) getIntent().getSerializableExtra("driver");
        }catch (Exception e){
            // Log...
        }
        if (companion!=null){
            takeDataCompanion(companion);
        }else if (driver!=null){
            takeDataDriver(driver);
        }
    }

    private void addRev() {
        if (driver!=null&&!add_rev_edit_text.getText().toString().equals("")) {
            FirebaseDatabase.getInstance().getReference().child("reviews")
                    .child(driver.getDate_create() + "")
                    .child(new Date().getTime() + "")
                    .setValue(add_rev_edit_text.getText().toString());
            add_rev_edit_text.setText("");
        }else if (companion!=null){
            FirebaseDatabase.getInstance().getReference().child("reviews")
                    .child(companion.getDate_create() + "")
                    .child(new Date().getTime() + "")
                    .setValue(add_rev_edit_text.getText().toString());
            add_rev_edit_text.setText("");
        }
    }

    private void takeDataDriver(Driver driver) {
        FirebaseDatabase.getInstance().getReference().child("reviews").child(driver.getDate_create()+"")
                .addValueEventListener(new ValueEventListener() {
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
        ArrayAdapter adapter = new ArrayAdapter(RewiewsDrivers.this,android.R.layout.simple_spinner_item
        ,list);
        listView.setAdapter(adapter);
    }

    private void takeDataCompanion(Companion companion) {
        FirebaseDatabase.getInstance().getReference().child("reviews").child(companion.getDate_create()+"")
                .addValueEventListener(new ValueEventListener() {
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
