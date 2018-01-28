package com.poputchic.android.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poputchic.android.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Snackbar.make(findViewById(R.id.map),"Удерживайте на точке",Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        final LatLng[] sydney = {null};
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                sydney[0] = latLng;
                mMap.addMarker(new MarkerOptions().position(sydney[0]).title("Точка"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney[0]));

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(sydney[0].latitude, sydney[0].longitude, 1);
                } catch (IOException e) {
                    //Log...
                }
                Intent intent = new Intent();
                try {
                    if (addresses!=null&&!addresses.get(0).equals("")){
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
