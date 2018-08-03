package com.poputchic.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.activities.reg_and_sign.SignInOrRegistration;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashScreen extends Activity {

    private Driver driver;
    private Companion companion;
    private AVLoadingIndicatorView pb_check;
    private Intent intent;
    private String D = "";
    private String C = "";
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pb_check = (AVLoadingIndicatorView)findViewById(R.id.pb_check);
        checkSavedData();
        //BLOCK_APP();
    }

    private void checkSavedData() {
        // создаем водителя и попутчика
        driver = null;
        companion = null;
        // создаем две стринги где будет лежать json ки, возможно!
        data = new Data(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    D = data.getDriverData(); // пытаемся получить водителя
                    C = data.getCompanionData(); // пытаемся получить попутчика
                    if (!D.equals("")){
                        // если нам пришла джейсонка водителя не нулевая то мы создаем водителя
                        createDriver(D); // создаем водителя из строки
                    }if (!C.equals("")){
                        // если нам пришла не пустая строка для попутчика
                        createCompanion(C); // создаем попутчика
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_check.hide();// вообще ничего не сохранено и скрываем прогресс
                            }
                        });
                        startActivity(new Intent(SplashScreen.this,SignInOrRegistration.class));
                    }
                }catch (Exception e){
                    startActivity(new Intent(SplashScreen.this,SignInOrRegistration.class));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb_check.hide();// вообще ничего не сохранено и скрываем прогресс
                        }
                    });
                }
            }
        }).start();

    } // проверка входили ли мы раньше в приложение и сохранены ли мы как попутчик или как водитель


    private void createCompanion(String s) {
        createIntent();
        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(s)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companion = dataSnapshot.getValue(Companion.class);
                        if (companion!=null){
                            intent.putExtra("companion",companion);
                            startActivity(intent);
                        }else{
                            //startActivity(new Intent(SplashScreen.this,SignInOrRegistration.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    } // получаем попутчика

    private void createDriver(String s) {
        createIntent();
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver = dataSnapshot.getValue(Driver.class);
                if (driver!=null){
                    intent.putExtra("driver",driver);
                    startActivity(intent);
                }else{
                    //startActivity(new Intent(SplashScreen.this,SignInOrRegistration.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }// создаем водителя

    private void createIntent(){
        driver = null; intent = null;
        intent = new Intent(this,MainListActivity.class);

    } // создаем интент
}
