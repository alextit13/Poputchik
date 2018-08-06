package com.poputchic.android.activities.reg_and_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.admin.AdminActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.wang.avi.AVLoadingIndicatorView;

public class SignIn extends Activity {

    private EditText    b_et_email,b_et_password;
    private Button      b_b_sign_in;
    private ImageView   image_back_signIn;
    //private ProgressBar pb_sign_in;
    private AVLoadingIndicatorView progress;

    private Driver driver;
    private Companion companion;

    private ScrollView scroll_sigm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        addListenerScrollToscroll_sigm();
        changeFonts(); // изменяем шрифты вьюх
        cleanData();
    }

    private void addListenerScrollToscroll_sigm() {
        scroll_sigm.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                image_back_signIn.setY(-(scroll_sigm.getScrollY()/20));
            }
        });
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.a_toolbar));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.b_b_sign_in));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.b_et_email));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.b_et_password));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.a_text_reg));
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.enter_your_data));
    }

    private void init(){
        progress = (AVLoadingIndicatorView)findViewById(R.id.pb_sign_in);
        progress.hide();
        b_et_email = (EditText) findViewById(R.id.b_et_email);
        b_et_password = (EditText) findViewById(R.id.b_et_password);

        scroll_sigm = (ScrollView)findViewById(R.id.scroll_sigm);

        image_back_signIn = (ImageView) findViewById(R.id.image_back_signIn);
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.b_b_sign_in:
                // sign in
                progress.show();
                signIn();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        if (checkCompleteFields()){
            if (b_et_email.getText().toString().equals("admin@admin.com")&&
                    b_et_password.getText().toString().equals("admin")){
                // sign admin
                Intent intent = new Intent(SignIn.this, AdminActivity.class);
                startActivity(intent);
            }
            driver = getDataDriver();
            companion = getDataCompanion();
        }else{
            progress.hide();
        }
    }

    private void cleanData() {
        Data data = new Data(this);
        data.saveSharedPreferenceDRIVER(null);
        data.saveSharedPreferenceCOMPANION(null);
    }

    private boolean checkCompleteFields() {
        boolean b = false;
        if (!b_et_email.getText().toString().equals("")
                &&!b_et_password.getText().toString().equals("")){
            b = true;
        }else{
            b = false;
            progress.hide();
            Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
        }
        return b;
    }

    public Driver getDataDriver() {
        driver = null;
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child("drivers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        if (data.getValue(Driver.class).getEmail().equals(b_et_email.getText().toString())
                                &&data.getValue(Driver.class).getPassword().equals(b_et_password.getText().toString())){
                            //Log.d(VARIABLES_CLASS.LOG_TAG,"dr = " + data.getValue(Driver.class));
                            driver = data.getValue(Driver.class);
                            goToIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.hide();
                    Toast.makeText(SignIn.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            progress.hide();
            Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
        return driver;
    }

    private void goToIntent() {
        if (driver!=null){
            Intent intent = new Intent(SignIn.this,MainListActivity.class);
            intent.putExtra("driver",driver);
            progress.hide();
            startActivity(intent);
        }
        if (companion!=null){
            Intent intent = new Intent(SignIn.this,MainListActivity.class);
            intent.putExtra("companion",companion);
            progress.hide();
            startActivity(intent);
        }
    }

    public Companion getDataCompanion() {
        companion = null;
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child("companion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        if (data.getValue(Companion.class).getEmail().equals(b_et_email.getText().toString())
                                &&data.getValue(Companion.class).getPassword().equals(b_et_password.getText().toString())){
                            companion = data.getValue(Companion.class);
                            goToIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.hide();
                    Toast.makeText(SignIn.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
        return companion;
    }
}
