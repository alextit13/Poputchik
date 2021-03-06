package com.poputchic.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.Cities;
import com.poputchic.android.map.MapsActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTravel extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private FusedLocationProviderClient mFusedLocationClient;

    private Driver driver;

    private String defaultLocation = "";
    private long milisecondsDate = 0;

    private int minute, hours, day, month, year;

    private double locality_lat = 0; // line for adress user location now - latitude
    private double locality_lon = 0; // line for adress user location now - longitude

    private ProgressBar f_pb_;
    private AutoCompleteTextView e_et_from, e_et_to;
    private EditText e_et_pointer_adress_1, e_et_pointer_adress_2, e_about, e_how_many_peoples;
    private Button e_b_time_start, /*e_b_time_finish*/
            e_b_cancel, e_b_go, e_b_date, b_onMap_start, b_onMap_finish;

    private String adress_from;
    private String adress_to;

    private List<Cities> listCities;

    private RelativeLayout add_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        init();
        changeFonts(); // изменяем шрифты на вьюхах
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                controller.myProfile(driver, null);
                break;
            case R.id.b_menu_2:
                //controller.exit();
                finish();
                break;
            case R.id.b_menu_3:
                //controller.addClick(driver,null);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(driver, null);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(driver, null);
                break;
        }
    }

    private void changeFonts() {
        /*FontsConteroller.changeFontToComfort(this, (TextView) findViewById(R.id.create_voyage));
        FontsConteroller.changeFontToComfort(this, e_et_from);
        FontsConteroller.changeFontToComfort(this, e_et_to);
        FontsConteroller.changeFontToComfort(this, e_et_pointer_adress_1);
        FontsConteroller.changeFontToComfort(this, e_et_pointer_adress_2);
        FontsConteroller.changeFontToComfort(this, e_about);
        FontsConteroller.changeFontToComfort(this, e_how_many_peoples);
        FontsConteroller.changeFontToComfort(this, e_b_time_start);
        FontsConteroller.changeFontToComfort(this, e_b_cancel);
        FontsConteroller.changeFontToComfort(this, e_b_go);
        FontsConteroller.changeFontToComfort(this, e_b_date);
        FontsConteroller.changeFontToComfort(this, b_onMap_start);
        FontsConteroller.changeFontToComfort(this, b_onMap_finish);*/
    } // изменяем шрифты на вьюхах

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    System.out.println("permission denied!");
                }
                break;
        }
    }

    private void init() {

        add_container = (RelativeLayout) findViewById(R.id.add_container);

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
        //e_b_time_finish = (Button) findViewById(R.id.e_b_time_finish);
        e_b_cancel = (Button) findViewById(R.id.e_b_cancel);
        e_b_go = (Button) findViewById(R.id.e_b_go);

        b_onMap_start = (Button) findViewById(R.id.b_onMap_start);
        b_onMap_finish = (Button) findViewById(R.id.b_onMap_finish);

        takeCoordinates();
        clickers();
        autocompleteCityes();
        getUserLocation();
    }

    private void getUserLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                if (addresses != null) {
                                    //System.out.println(Arrays.asList(addresses.get(0).getAddressLine(0)));
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

    private void takeCoordinates() {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission, Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_PERMISSION);
            }else{

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
        /*e_b_time_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTime(e_b_time_finish);
            }
        });*/
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
                    ,adress_from,adress_to, milisecondsDate + ""
                    ,"",driver.getDate_create()+"",e_about.getText().toString(),new Date().getTime()+"");
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

        if (!e_et_from.getText().toString().equals("") && !e_et_pointer_adress_1.getText().toString().equals("") &&
                !e_et_to.getText().toString().equals("") && !e_et_pointer_adress_2.getText().toString().equals("")){
            adress_from = e_et_from.getText().toString() + " " + e_et_pointer_adress_1.getText().toString();
            adress_to = e_et_to.getText().toString() + " " + e_et_pointer_adress_2.getText().toString();
        }else{
            Snackbar.make(e_b_go,"Заполните все поля",Snackbar.LENGTH_LONG).show();
        }
    }

    private void map(Button b) {
        switch (b.getId()){
            case R.id.b_onMap_start:
                //
                Intent intent_1 = new Intent(AddTravel.this, MapsActivity.class);
                intent_1.putExtra("location_user_log",locality_lon);
                intent_1.putExtra("location_user_lat",locality_lat);
                startActivityForResult(intent_1,1);
                break;
            case R.id.b_onMap_finish:
                //
                Intent intent_2 = new Intent(AddTravel.this, MapsActivity.class);
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
        if (requestCode==1){
            // start point
            try {
                adress_from = data.getStringExtra("adress");
            }catch (RuntimeException e) {
                e.printStackTrace();
            }

            String stringFromData = "";

            try {
                stringFromData = data.getStringExtra("adress");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (stringFromData!=null && !stringFromData.equals("")){
                e_et_from.setText(data.getStringExtra("city"));
                e_et_pointer_adress_1.setText(adress_from);
            }

            //Log.d(VARIABLES_CLASS.LOG_TAG,"req = " +  data.getParcelableExtra("coor"));
        }else if (requestCode==2){
            // finish point
            try {
                adress_to = data.getStringExtra("adress");
            }catch (RuntimeException e) {
                e.printStackTrace();
            }

            String dataFromData = "";
            try {
                dataFromData = data.getStringExtra("adress");
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (dataFromData!=null && !dataFromData.equals("")){
                e_et_to.setText(data.getStringExtra("city"));
                e_et_pointer_adress_2.setText(adress_to);
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

                i1 = i1 + 1;

                String string_date = i2+"-"+i1+"-"+i;
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date d = f.parse(string_date);
                    milisecondsDate = d.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                year = i; month = i1; day = i2;
                e_b_date.setText(i2 + "." + i1 + "."+i);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void clickTime(final Button b) {
        //Log.d(VARIABLES_CLASS.LOG_TAG,"click time");
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (b.getId()) {
                    case R.id.e_b_time_start:

                        milisecondsDate = milisecondsDate + (i * 3600000) + (i1 * 60000);

                        // ...
                        minute = i1;
                        hours = i;
                        e_b_time_start.setText(i + "." + i1);
                        break;
                    /*case R.id.e_b_time_finish:
                        //
                        e_b_time_finish.setText(i + "." + i1);
                        break;*/
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
