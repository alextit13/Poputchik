package com.poputchic.android.bottom_toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.poputchic.android.R;
import com.poputchic.android.activities.AddTravel;
import com.poputchic.android.activities.AddZayavka;
import com.poputchic.android.activities.MyTravelsCompanion;
import com.poputchic.android.activities.Reviews;
import com.poputchic.android.activities.person_rooms.PersonRoomCompanion;
import com.poputchic.android.activities.person_rooms.PersonRoomDriver;
import com.poputchic.android.models.Data;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

public class BottomToolbarController implements BottomToolbarListener{

    private Activity activity;
    private ImageView b_menu_1, b_menu_2, b_menu_3, b_menu_4, b_menu_5;
    private View view;

    public BottomToolbarController(Activity activity) {
        this.activity = activity;
        initItemsViews();
    }

    private void initItemsViews() {
        b_menu_1 = (ImageView) activity.findViewById(R.id.b_menu_1);
        b_menu_2 = (ImageView) activity.findViewById(R.id.b_menu_2);
        b_menu_3 = (ImageView) activity.findViewById(R.id.b_menu_3);
        b_menu_4 = (ImageView) activity.findViewById(R.id.b_menu_4);
        b_menu_5 = (ImageView) activity.findViewById(R.id.b_menu_5);
    }

    @Override
    public void myProfile(Driver driver, Companion companion) {
        if (driver != null) {
            //goToPrivateRoomDriver(driver);
            Intent intent = new Intent(activity, PersonRoomDriver.class);
            intent.putExtra("driver", driver);
            activity.startActivity(intent);
        } else if (companion != null) {
            //goToPrivateRoomCompanion(companion);
            Intent intent = new Intent(activity, PersonRoomCompanion.class);
            intent.putExtra("companion", companion);
            activity.startActivity(intent);
        }
    }

    @Override
    public void exit() {
        new AlertDialog.Builder(activity)
                .setTitle("Выход")
                .setMessage("Вы действительно хотите выйти?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //the user wants to leave - so dismiss the dialog and exit
                        Data data = new Data(activity);
                        data.saveSharedPreferenceDRIVER(null);
                        data.saveSharedPreferenceCOMPANION(null);


                        activity.finish();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .show();
    }

    @Override
    public void addClick(Driver driver, Companion companion) {
        if (driver != null) {
            //addTravel();
            Intent intent = new Intent(activity, AddTravel.class);
            intent.putExtra("driver", driver);
            activity.startActivity(intent);
        } else if (companion != null) {
            //addZayavka(companion);
            Intent intent = new Intent(activity, AddZayavka.class);
            intent.putExtra("companion", companion);
            activity.startActivity(intent);
        }
    }

    @Override
    public void imCompanoin(Driver driver, Companion companion) {
        if (companion != null) {
            Intent intent = new Intent(activity, MyTravelsCompanion.class);
            intent.putExtra("companion", companion);
            activity.startActivity(intent);
        }else if (driver!=null){
            Intent intent = new Intent(activity, MyTravelsCompanion.class);
            intent.putExtra("driver", driver);
            activity.startActivity(intent);
        }
    }

    @Override
    public void usersList(Driver driver, Companion companion) {
        Intent intent = new Intent(activity,Reviews.class);
        if (driver!=null){
            intent.putExtra("driver",driver);
            activity.startActivity(intent);
        }else if (companion!=null){
            intent.putExtra("companion",companion);
            activity.startActivity(intent);
        }
    }
}
