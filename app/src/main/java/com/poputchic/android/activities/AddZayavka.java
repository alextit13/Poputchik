package com.poputchic.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.poputchic.android.classes.enums.Cities;
import com.poputchic.android.map.MapsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddZayavka extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    //private Driver driver;

    private String defaultLocation = "";
    private FusedLocationProviderClient mFusedLocationClient;

    private ProgressBar f_pb_;
    private EditText  e_et_pointer_adress_2, e_about,e_et_from,e_et_pointer_adress_1,e_et_to;
    private Button e_b_time_start, e_b_cancel, e_b_go, e_b_date, b_onMap_start, b_onMap_finish;

    private double locality_lat = 0; // line for adress user location now - latitude
    private double locality_lon = 0; // line for adress user location now - longitude

    private long dateFrom = 0;

    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;

    int DIALOG_DATE = 2;
    int myYear = 2011;
    int myMonth = 02;
    int myDay = 03;

    private String adress_from;
    private String adress_to;

    private Companion companion;

    //private FrameLayout add_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zayavka);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        init();
        changeFonts();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //Log.d(VARIABLES_CLASS.LOG_TAG,"location = " + location);
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            // In this sample, get just a single address.
                                            1);
                                } catch (IOException e) {

                                }
                                if (addresses!=null){
                                    System.out.println(Arrays.asList(addresses.get(0).getAddressLine(0)));
                                    e_et_pointer_adress_1.setText(addresses.get(0).getAddressLine(0));
                                    e_et_from.setText(addresses.get(0).getLocality());
                                    locality_lon = addresses.get(0).getLongitude();
                                    locality_lat = addresses.get(0).getLatitude();
                                }
                            }
                        }
                    });
        }
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                controller.myProfile(null,companion);
                break;
            case R.id.b_menu_2:
                finish();
                //controller.exit();
                break;
            case R.id.b_menu_3:
                //controller.addClick(null,companion);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(null,companion);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(null,companion);
                break;
        }
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.activity_add_zayavka_logo));
        FontsDriver.changeFontToComfort(this,e_et_pointer_adress_2);
        FontsDriver.changeFontToComfort(this,e_about);
        FontsDriver.changeFontToComfort(this,e_et_from);
        FontsDriver.changeFontToComfort(this,e_et_pointer_adress_1);
        FontsDriver.changeFontToComfort(this,e_et_to);
        FontsDriver.changeFontToComfort(this,e_b_time_start);
        FontsDriver.changeFontToComfort(this,e_b_cancel);
        FontsDriver.changeFontToComfort(this,e_b_go);
        FontsDriver.changeFontToComfort(this,e_b_date);
        FontsDriver.changeFontToComfort(this,b_onMap_start);
        FontsDriver.changeFontToComfort(this,b_onMap_finish);
    }

    void getLocation(){


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    System.out.println("permission denied!");
                }
                break;
        }
    }

    private void init() {

        //add_container = (FrameLayout) findViewById(R.id.add_container);

       companion = (Companion) getIntent().getSerializableExtra("companion");

        e_about = (EditText) findViewById(R.id.e_about_z);

        f_pb_ = (ProgressBar) findViewById(R.id.f_pb_z);
        f_pb_.setVisibility(View.INVISIBLE);

        e_b_date = (Button) findViewById(R.id.e_b_date_z);

        e_et_from = (EditText) findViewById(R.id.e_et_from_z);
        e_et_to = (EditText) findViewById(R.id.e_et_to_z);

        e_et_pointer_adress_1 = (EditText) findViewById(R.id.e_et_pointer_adress_1_z);
        e_et_pointer_adress_2 = (EditText) findViewById(R.id.e_et_pointer_adress_2_z);

        e_b_time_start = (Button) findViewById(R.id.e_b_time_start_z);
        e_b_cancel = (Button) findViewById(R.id.e_b_cancel_z);
        e_b_go = (Button) findViewById(R.id.e_b_go_z);

        b_onMap_start = (Button) findViewById(R.id.b_onMap_start_z);
        b_onMap_finish = (Button) findViewById(R.id.b_onMap_finish_z);

        takeCoordinates();
        clickers();
        //autocompleteCityes();
    }

    private void takeCoordinates() {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission, Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_PERMISSION);
            }else{
                //read location
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickers() {
        e_b_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        e_b_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTime(e_b_time_start);
            }
        });
        e_b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel...
                new AlertDialog.Builder(AddZayavka.this).setTitle("Выход")
                        .setMessage("Вы действительно хотите выйти не сохранив изменения?")
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create().show();
            }
        });
        e_b_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go...
                createZayavka();
            }
        });
        b_onMap_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map(b_onMap_start);
            }
        });
        b_onMap_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map(b_onMap_finish);
            }
        });
    }

    private void createZayavka() {
        createAdresses();
        if (adress_from!=null||adress_to!=null&&!e_et_from.getText().toString().equals("")
                &&!e_et_to.getText().toString().equals("")){
            ZayavkaFromCompanion ZFC = new ZayavkaFromCompanion(null,new Date().getTime()+"",adress_from+", "+e_et_from.getText().toString()
                    ,adress_to+", " + e_et_to.getText().toString(),/*e_b_time_start.getText().toString()*/

                    new Date(myYear,myMonth,myDay,myHour,myMinute).getTime()+""

            ,"0",e_about.getText().toString(),companion.getDate_create()+"");
            pushToFirebase(ZFC);
        }else{
            f_pb_.setVisibility(View.INVISIBLE);

            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
        }
    }

    private void pushToFirebase(ZayavkaFromCompanion z) {
        FirebaseDatabase.getInstance().getReference().child("zayavki_from_companoins").child(z.getDate()+"")
                .setValue(z).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddZayavka.this, "Поездка успешно создана!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }



    private void createAdresses() {
        if (!b_onMap_start.getText().toString().equals("Карта")){
            // если на кнопке адрес
            //Log.d(VARIABLES_CLASS.LOG_TAG,"11");
            adress_from = b_onMap_start.getText().toString();
        }else if (!e_et_from.getText().toString().equals("")&&
                !e_et_pointer_adress_1.getText().toString().equals("")) {
            //Log.d(VARIABLES_CLASS.LOG_TAG,"12");
            {
                //Log.d(VARIABLES_CLASS.LOG_TAG,"13");
                adress_from = e_et_from.getText().toString() + "," + e_et_pointer_adress_1.getText().toString();
            }
        }else{
            //Log.d(VARIABLES_CLASS.LOG_TAG,"4");
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
            f_pb_.setVisibility(View.INVISIBLE);
        }

        if (!b_onMap_finish.getText().toString().equals("Карта")){
            // если на кнопке адрес
            //Log.d(VARIABLES_CLASS.LOG_TAG,"21");
            adress_to = b_onMap_finish.getText().toString();
        }else if (!e_et_to.getText().toString().equals("")&&
                !e_et_pointer_adress_2.getText().toString().equals("")) {
            //Log.d(VARIABLES_CLASS.LOG_TAG,"22");
            {
                //Log.d(VARIABLES_CLASS.LOG_TAG,"23");
                adress_to = e_et_to.getText().toString() + "," + e_et_pointer_adress_2.getText().toString();
            }
        }else{
            //Log.d(VARIABLES_CLASS.LOG_TAG,"24");
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
            f_pb_.setVisibility(View.INVISIBLE);

        }
    }

    private void map(Button b) {
        switch (b.getId()){
            case R.id.b_onMap_start_z:
                //
                Intent intent_1 = new Intent(AddZayavka.this, MapsActivity.class);
                intent_1.putExtra("location_user_log",locality_lon);
                intent_1.putExtra("location_user_lat",locality_lat);
                startActivityForResult(intent_1,1);
                break;
            case R.id.b_onMap_finish_z:
                //
                Intent intent_2 = new Intent(AddZayavka.this, MapsActivity.class);
                intent_2.putExtra("location_user_log",locality_lon);
                intent_2.putExtra("location_user_lat",locality_lat);
                startActivityForResult(intent_2,2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String city = "";
        city = data.getStringExtra("city");
        if (requestCode==1){
            // start point
            adress_from = data.getStringExtra("adress");

            if (data.getStringExtra("adress")!=null){
                e_et_from.setText(city);
                e_et_pointer_adress_1.setText(adress_from);

                //e_et_pointer_adress_1.setText(adress_from);
            }

            //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  data.getParcelableExtra("coor"));
        }else if (requestCode==2){
            // finish point
            adress_to = data.getStringExtra("adress");
            if (data.getStringExtra("adress")!=null){
                e_et_to.setText(city);
                e_et_pointer_adress_2.setText(adress_to);
                //e_et_pointer_adress_2.setText(adress_to);
            }
            //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  data.getParcelableExtra("coor"));
        }
        //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  requestCode);
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myYear = i;
                myMonth = i1;
                myDay = i2;
                e_b_date.setText(i2 + "." + i1 + "."+i);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void clickTime(final Button b) {
        //Log.d(VARIABLES_CLASS.LOG_TAG,"click time");
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (b.getId()) {
                    case R.id.e_b_time_start_z:
                        // ...
                        myHour = i;
                        myMinute = i1;
                        e_b_time_start.setText(i + "." + i1);
                        break;
                    default:
                        break;
                }
            }
        },12,00,true).show();
    }
}