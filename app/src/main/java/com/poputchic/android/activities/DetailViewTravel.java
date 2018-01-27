package com.poputchic.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Travel;

public class DetailViewTravel extends AppCompatActivity {

    private Companion companion;
    private Travel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_travel);

        init();
    }

    private void init() {

    }
}
