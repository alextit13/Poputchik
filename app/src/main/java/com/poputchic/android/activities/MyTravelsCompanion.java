package com.poputchic.android.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Zayavka;

import java.util.ArrayList;

public class MyTravelsCompanion extends Activity {

    private ImageView back_button_my_travels;
    private ListView list_my_z;
    private ArrayList<Zayavka>listMyZ = new ArrayList<>();
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
        FirebaseDatabase.getInstance().getReference().child("complete_travels");
    }
}
