package com.poputchic.android.activities.person_rooms.my_travels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;

public class MyFinishesTravels extends Activity {

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
        changeFonts();// изменяем шрифт вьюх
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.activity_my_finishes_travel_label));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.logo_my_travels));
    }
}
