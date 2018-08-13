package com.poputchic.android.presenters.splashScreenPresenter;

import android.app.Activity;

import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

public class SplashScreenPresenter implements Driver.TakeSAveDriver, Companion.TakeSAveCompanion {

    private Activity activity;

    public SplashScreenPresenter(Activity activity) {
        this.activity = activity;
    }

    public void checkSavedData() {
        Driver driver = new Driver();
        driver.getSaveDriver(this,activity);
    } // проверка входили ли мы раньше в приложение и сохранены ли мы как попутчик или как водитель

    @Override
    public void getDriver(Driver driver) {
        ResultDriver resultDriver = (ResultDriver)activity;
        if (driver == null)
            //resultDriver.getDriverResult(null);
            checkSaveCompanion();
        else
            resultDriver.getDriverResult(driver);
    }

    private void checkSaveCompanion() {
        Companion companion = new Companion();
        companion.getSaveCompanion(this,activity);
    }

    @Override
    public void getCompanion(Companion companion) {
        ResultCompanion resultCompanion = (ResultCompanion)activity;
        resultCompanion.getCompanionResult(companion);
    }

    public interface ResultDriver{
        void getDriverResult(Driver driver);
    }

    public interface ResultCompanion{
        void getCompanionResult(Companion companion);
    }
}
