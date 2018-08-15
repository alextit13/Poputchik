package com.poputchic.android.views.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.poputchic.android.R;
import com.poputchic.android.activities.main_list.MainListActivity;
import com.poputchic.android.views.signInOrRegistration.SignInOrRegistration;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.presenters.splashScreenPresenter.SplashScreenPresenter;
import com.poputchic.android.views.registration.Registration;

public class SplashScreen extends Activity implements SplashScreenPresenter.ResultDriver, SplashScreenPresenter.ResultCompanion {

    private Intent intent;
    private SplashScreenPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSplashPresenter = new SplashScreenPresenter(this);
        checkSavedData(); // check save data in sharedPrefs
    }

    private void checkSavedData() {
        mSplashPresenter.checkSavedData();
    }

    @Override
    public void getDriverResult(Driver driver) {
        if (driver == null)
            startActivity(new Intent(this, Registration.class));
        else {
            intent = new Intent(this,MainListActivity.class);
            intent.putExtra("driver",driver);
            startActivity(intent);
        }
    }

    @Override
    public void getCompanionResult(Companion companion) {
        if (companion == null)
            startActivity(new Intent(this, SignInOrRegistration.class));
        else {
            intent = new Intent(this,MainListActivity.class);
            intent.putExtra("companion",companion);
            startActivity(intent);
        }
    }
}
