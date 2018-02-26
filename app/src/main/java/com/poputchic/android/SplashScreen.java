package com.poputchic.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.poputchic.android.reg_and_sign.Registration;
import com.poputchic.android.reg_and_sign.SignInOrRegistration;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        BLOCK_APP();
        init();
    }

    private void init() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0000);
                    Intent intent = new Intent(SplashScreen.this, SignInOrRegistration.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private void BLOCK_APP() {
        // block
    }
}
