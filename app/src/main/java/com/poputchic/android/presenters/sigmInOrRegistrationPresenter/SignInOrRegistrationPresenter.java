package com.poputchic.android.presenters.sigmInOrRegistrationPresenter;

import android.app.Activity;
import android.content.Intent;

import com.poputchic.android.activities.reg_and_sign.SignIn;
import com.poputchic.android.views.registration.Registration;

public class SignInOrRegistrationPresenter implements ISignInOrRegistrationPresenter{

    private Activity activity;
    private Intent intent;

    public SignInOrRegistrationPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void signIn() {
        intent = new Intent(activity, SignIn.class);
        StarterInterfaceSelect starterInterfaceSelect = (StarterInterfaceSelect)activity;
        starterInterfaceSelect.result(intent);
    }

    @Override
    public void registrationIn() {
        intent = new Intent(activity, Registration.class);
        StarterInterfaceSelect starterInterfaceSelect = (StarterInterfaceSelect)activity;
        starterInterfaceSelect.result(intent);
    }

    public interface StarterInterfaceSelect{
        void result(Intent intent); // 1 - signIn, 2 - registration
    }

}
