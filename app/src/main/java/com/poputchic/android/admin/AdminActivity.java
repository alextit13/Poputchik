package com.poputchic.android.admin;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.poputchic.android.R;
import com.poputchic.android.admin.fragments.FragmentAllMessage;
import com.poputchic.android.admin.fragments.FragmentOneMessage;

public class AdminActivity extends Activity {

    private FrameLayout container;
    private Button btn_message_one_user,btn_message_all_users;

    private FragmentOneMessage fragment_one;
    private FragmentAllMessage fragment_all;
    private FragmentTransaction fTrans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
    }

    private void init() {
        fragment_one = new FragmentOneMessage();
        fragment_one.FragmentOneMessage(AdminActivity.this);
        fragment_all = new FragmentAllMessage();
        fragment_all.FragmentAllMessage(AdminActivity.this);


        container = (FrameLayout) findViewById(R.id.container);
        btn_message_one_user = (Button) findViewById(R.id.btn_message_one_user);
        btn_message_all_users = (Button) findViewById(R.id.btn_message_all_users);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.container,fragment_one);

    }

    public void clickAdmin(View view) {
        FragmentTransaction fTrans_two = getFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.btn_message_one_user:
                //
                fTrans_two.replace(R.id.container,fragment_one);
                break;
            case R.id.btn_message_all_users:
                //
                fTrans_two.replace(R.id.container,fragment_all);
                break;
            default:
                break;
        }
        fTrans_two.addToBackStack(null);
        fTrans_two.commit();
    }
}
