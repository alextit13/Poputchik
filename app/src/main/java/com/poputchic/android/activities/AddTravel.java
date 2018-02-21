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
    private ProgressBar f_pb_;
    private AutoCompleteTextView e_et_from, e_et_to;
    private EditText e_et_pointer_adress_1, e_et_pointer_adress_2, e_about, e_how_many_peoples;
    private Button e_b_time_start, e_b_cancel, e_b_go, e_b_date, b_onMap_start, b_onMap_finish;
    private String adress_from;
    private String adress_to;
    private List<Cities> listCities;
    private FrameLayout add_container;
    private FusedLocationProviderClient mFusedLocationClient;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        init();

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
                                    e_et_from.setText(addresses.get(0).getLocality());
                                    e_et_pointer_adress_1.setText(addresses.get(0).getAddressLine(0));
                                    //e_et_from.setText(addresses.get(0).getAddressLine(0)+"");
                                }
                            }
                        }
                    });
        }
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
                    ,"0",driver.getDate_create()+"",e_about.getText().toString(),new Date().getTime()+"");
            f_pb_.setVisibility(View.VISIBLE);
            add_container.setAlpha(.3f);
            //pushToFirebase(travel);
            getNumZayavka(travel);
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

    private void getNumZayavka(final Travel t) {
        FirebaseDatabase.getInstance().getReference().child("travels")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    if (data.getValue(Travel.class).getDriver_create().equals(driver.getDate_create())){
                                        num++;
                                    }
                                }
                                if (num+1<=2){
                                    pushToFirebase(t);
                                }else{
                                    f_pb_.setVisibility(View.INVISIBLE);
                                    add_container.setAlpha(1f);
                                    Toast.makeText(AddTravel.this, "В работе уже две поездки!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
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
            try {
                String city = "";
                city = data.getStringExtra("city");
                adress_from = data.getStringExtra("adress");
                if (data.getStringExtra("adress")!=null){
                    e_et_from.setText(city);
                    e_et_pointer_adress_1.setText(adress_from);
                }
            }catch (Exception e){

            }

        }else if (requestCode==2){
            // finish point
            try {
                adress_to = data.getStringExtra("adress");
                String city = "";
                city = data.getStringExtra("city");

                if (data.getStringExtra("adress")!=null){
                    e_et_pointer_adress_2.setText(adress_to);
                    e_et_to.setText(city);
                    //e_et_pointer_adress_2.setText(adress_to);
                }
            }catch (Exception e){

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
