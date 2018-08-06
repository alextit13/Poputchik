package com.poputchic.android.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double locality_user_lon;
    private double locality_user_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        locality_user_lon = getIntent().getDoubleExtra("location_user_log",37.6155);
        locality_user_lat = getIntent().getDoubleExtra("location_user_lat",55.7522);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Snackbar.make(findViewById(R.id.map), "Удерживайте на точке", Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(locality_user_lat,locality_user_lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        final LatLng[] s = {null};
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //sydney[0] = latLng;
                s[0] = latLng;
                mMap.addMarker(new MarkerOptions().position(s[0]).title("Точка"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(longClickLocation[0]));

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(s[0].latitude, s[0].longitude, 1);
                    //Log.d(VARIABLES_CLASS.LOG_TAG,"address = " + addresses.get(0));
                } catch (IOException e) {
                    //Log...
                }
                Intent intent = new Intent();
                try {
                    if (s[0]!=null&&!s[0].equals("")){
                        intent.putExtra("city",  addresses.get(0).getLocality());
                        intent.putExtra("adress", addresses.get(0).getAddressLine(0));
                    }
                }catch (Exception e){
                    //Log...
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
