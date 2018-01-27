package com.poputchic.android.activities.person_rooms.my_travels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.poputchic.android.R;

public class MyFinishesTravels extends AppCompatActivity {

    private ImageView pr_iv_my_travels_finish_c;
    private ListView companion_finish_travels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_finishes_travels);

        init();
        clisker();
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
        pr_iv_my_travels_finish_c = (ImageView) findViewById(R.id.pr_iv_my_travels_finish_c);
        companion_finish_travels = (ListView) findViewById(R.id.companion_finish_travels);
    }
}
