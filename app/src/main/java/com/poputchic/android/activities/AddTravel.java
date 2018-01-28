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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TimePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.enums.Cities;
import com.poputchic.android.map.MapsActivity;

import org.ankit.gpslibrary.MyTracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTravel extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    private Driver driver;

    private String defaultLocation = "";

    private ProgressBar f_pb_;
    private AutoCompleteTextView e_et_from, e_et_to;
    private EditText e_et_pointer_adress_1, e_et_pointer_adress_2, e_about, e_how_many_peoples;
    private Button e_b_time_start, e_b_time_finish, e_b_cancel, e_b_go, e_b_date, b_onMap_start, b_onMap_finish;

    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;

    int DIALOG_DATE = 2;
    int myYear = 2011;
    int myMonth = 02;
    int myDay = 03;

    private String adress_from;
    private String adress_to;

    private List<Cities> listCities;

    private FrameLayout add_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        init();
    }

    void getLocation(){
        /*MyTracker tracker=new MyTracker(this);
//        Log.d(VARIABLES_CLASS.LOG_TAG,"tracker.getLatitude() = " + tracker.getLatitude());
//        Log.d(VARIABLES_CLASS.LOG_TAG,"tracker.getLongitude() = " + tracker.getLongitude());
//        Log.d(VARIABLES_CLASS.LOG_TAG,"tracker.getLocation() = " + tracker.getLocation());

        defaultLocation = tracker.getLocation()+"";

        double lat = tracker.getLatitude();
        double lon = tracker.getLongitude();

        Log.d(VARIABLES_CLASS.LOG_TAG,"defaultLocation = " + defaultLocation);
        e_et_pointer_adress_1.setText("Координаты отправления: " + lat+", "+lon);

        *//*System.out.println(tracker.getLatitude());
        System.out.println(tracker.getLongitude());
        System.out.println(tracker.getLocation());
        System.out.println(tracker.address);
        System.out.println(tracker.cityName);
        System.out.println(tracker.state);
        System.out.println(tracker.countryName);
        System.out.println(tracker.countryCode);
        System.out.println(tracker.ipAddress);
        System.out.println(tracker.macAddress);*//*

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        *//*Log.d(VARIABLES_CLASS.LOG_TAG,"lat = " + lat);
        Log.d(VARIABLES_CLASS.LOG_TAG,"lon = " + lon);
        */


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

        add_container = (FrameLayout) findViewById(R.id.add_container);

        driver = (Driver) getIntent().getSerializableExtra("driver");

        e_about = (EditText) findViewById(R.id.e_about);

        f_pb_ = (ProgressBar) findViewById(R.id.f_pb_);
        f_pb_.setVisibility(View.INVISIBLE);

        e_b_date = (Button) findViewById(R.id.e_b_date);

        e_et_from = (AutoCompleteTextView) findViewById(R.id.e_et_from);
        e_et_to = (AutoCompleteTextView) findViewById(R.id.e_et_to);

        e_et_pointer_adress_1 = (EditText) findViewById(R.id.e_et_pointer_adress_1);
        e_et_pointer_adress_2 = (EditText) findViewById(R.id.e_et_pointer_adress_2);
        e_how_many_peoples = (EditText) findViewById(R.id.e_how_many_peoples);

        e_b_time_start = (Button) findViewById(R.id.e_b_time_start);
        e_b_time_finish = (Button) findViewById(R.id.e_b_time_finish);
        e_b_cancel = (Button) findViewById(R.id.e_b_cancel);
        e_b_go = (Button) findViewById(R.id.e_b_go);

        b_onMap_start = (Button) findViewById(R.id.b_onMap_start);
        b_onMap_finish = (Button) findViewById(R.id.b_onMap_finish);

        takeCoordinates();
        clickers();
        autocompleteCityes();
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
        e_b_time_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTime(e_b_time_finish);
            }
        });
        e_b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel...
                new AlertDialog.Builder(AddTravel.this).setTitle("Выход")
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
                createTravel();
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

    private void createTravel() {
        createAdresses();
        if (adress_from!=null||adress_to!=null){
            Travel travel = new Travel(0,Integer.parseInt(e_how_many_peoples.getText().toString())
                    ,adress_from,adress_to,e_b_time_start.getText().toString()
                    ,e_b_time_finish.getText().toString(),driver.getDate_create()+"",e_about.getText().toString(),new Date().getTime()+"");
            f_pb_.setVisibility(View.VISIBLE);
            add_container.setAlpha(.3f);
            pushToFirebase(travel);
        }else{
            f_pb_.setVisibility(View.INVISIBLE);
            add_container.setAlpha(1f);
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
        }
    }

    private void pushToFirebase(Travel t) {
        FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create()+"")
                .setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(e_b_go,"Поездка успешно создана!",Snackbar.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void createAdresses() {
        if (!b_onMap_start.getText().toString().equals("Карта")){
            // если на кнопке адрес
            Log.d(VARIABLES_CLASS.LOG_TAG,"11");
            adress_from = b_onMap_start.getText().toString();
        }else if (!e_et_from.getText().toString().equals("")&&
                !e_et_pointer_adress_1.getText().toString().equals("")) {
            Log.d(VARIABLES_CLASS.LOG_TAG,"12");
            {
                Log.d(VARIABLES_CLASS.LOG_TAG,"13");
                adress_from = e_et_from.getText().toString() + "," + e_et_pointer_adress_1.getText().toString();
            }
        }else{
            Log.d(VARIABLES_CLASS.LOG_TAG,"4");
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
            f_pb_.setVisibility(View.INVISIBLE);
            add_container.setAlpha(1f);
        }

        if (!b_onMap_finish.getText().toString().equals("Карта")){
            // если на кнопке адрес
            Log.d(VARIABLES_CLASS.LOG_TAG,"21");
            adress_to = b_onMap_finish.getText().toString();
        }else if (!e_et_to.getText().toString().equals("")&&
                !e_et_pointer_adress_2.getText().toString().equals("")) {
            Log.d(VARIABLES_CLASS.LOG_TAG,"22");
            {
                Log.d(VARIABLES_CLASS.LOG_TAG,"23");
                adress_to = e_et_to.getText().toString() + "," + e_et_pointer_adress_2.getText().toString();
            }
        }else{
            Log.d(VARIABLES_CLASS.LOG_TAG,"24");
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
            f_pb_.setVisibility(View.INVISIBLE);
            add_container.setAlpha(1f);
        }
    }

    private void map(Button b) {
        switch (b.getId()){
            case R.id.b_onMap_start:
                //
                Intent intent_1 = new Intent(AddTravel.this, MapsActivity.class);
                startActivityForResult(intent_1,1);
                break;
            case R.id.b_onMap_finish:
                //
                Intent intent_2 = new Intent(AddTravel.this, MapsActivity.class);
                startActivityForResult(intent_2,2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1){
            // start point
            adress_from = data.getStringExtra("adress");
            if (data.getStringExtra("adress")!=null){
                b_onMap_start.setText(adress_from);
            }

            //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  data.getParcelableExtra("coor"));
        }else if (requestCode==2){
            // finish point
            adress_to = data.getStringExtra("adress");
            if (data.getStringExtra("adress")!=null){
                b_onMap_finish.setText(adress_to);
            }
            //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  data.getParcelableExtra("coor"));
        }
        //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  requestCode);
    }

    private void selectDate() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                e_b_date.setText(i2 + "." + i1 + "."+i);
            }
        },2018,1,1).show();
    }

    private void clickTime(final Button b) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"click time");
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (b.getId()) {
                    case R.id.e_b_time_start:
                        // ...
                        e_b_time_start.setText(i + "." + i1);
                        break;
                    case R.id.e_b_time_finish:
                        //
                        e_b_time_finish.setText(i + "." + i1);
                        break;
                    default:
                        break;
                }
            }
        },12,00,true).show();
    }

    private void autocompleteCityes(){
        listCities = Arrays.asList(Cities.values());
        e_et_from.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,listCities));
        e_et_to.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,listCities));
    }
}
