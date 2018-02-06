package com.poputchic.android.activities.find;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.poputchic.android.R;

public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
    }

    public void clickButton(View view) {
        switch (view.getId()){
            case R.id.btnTEST:

        }
    }
}
