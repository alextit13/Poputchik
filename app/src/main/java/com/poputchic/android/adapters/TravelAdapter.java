package com.poputchic.android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.Zayavka;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TravelAdapter extends BaseAdapter{

    Context ctx;
    boolean check = true;
    LayoutInflater lInflater;
    ArrayList<Travel> objects;

    TextView tv_date,d_tv_from,d_tv_to,d_tv_about,places,
            name_driver_and_year,car,rating_driver,review_driver,finish_travels,number,review_driver_c;
    CircleImageView circleImageView;
    ImageView add_to_company;
    Companion companion;
    int swich = 0;
    RelativeLayout back;
    Driver driver;
    ArrayList<Driver>lisrDRVR;
/*
    public TravelAdapter(){

    }*/

    public TravelAdapter(Context context, ArrayList<Travel> products, Companion c) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        companion = c;
    }

    public TravelAdapter(Context context, ArrayList<Travel> products, Companion c,ArrayList<Driver> lisrDrivers) {
        lisrDRVR = lisrDrivers;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        companion = c;
    }

    public TravelAdapter(Context context, ArrayList<Travel> products, Companion c,int requestCode_swich) {
        swich = requestCode_swich;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        companion = c;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (convertView == null) {
            view = lInflater.inflate(R.layout.driver_list_item, parent, false);
        }

        Travel t = getProduct(position);

        init(view);

        if (t!=null){
            if (t.getTime_from()!=null){
                String d = "";
                Date date = new Date(Long.parseLong(t.getTime_from()));
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG,  Locale.getDefault());
                d = dateFormat.format(date);
                tv_date.setText(d);
            }
            if (t.getFrom()!=null){d_tv_from.setText(t.getFrom());}
            if (t.getTo()!=null){d_tv_to.setText(t.getTo());}
            if (t.getAbout_travel()!=null){d_tv_about.setText(t.getAbout_travel());}
            if (t.getPlaces()!=0){places.setText("Свободно мест: "+(t.getPlaces() - t.getCompanion()));}
            if (swich==1){
                add_to_company.setVisibility(View.INVISIBLE);
                places.setVisibility(View.INVISIBLE);
                back.setBackgroundColor(Color.parseColor("#FFC8FFBE"));
            }
        }
        takeDriver(position);
        clicker(t);
        return view;
    }

    int reviews = 0;

    private void takeDriver(int position){
        Log.d(VARIABLES_CLASS.LOG_TAG,"position = " + position);
        if (lisrDRVR!=null&&!lisrDRVR.isEmpty()){
            driver = lisrDRVR.get(position);
            Log.d(VARIABLES_CLASS.LOG_TAG,"driver = " + driver);
            if (driver.getName()!=null&&driver.getYear()!=0){name_driver_and_year.setText(driver.getName()+", "+driver.getYear());}
            if (driver.getName_car()!=null&&driver.getYear_car()!=null){car.setText(driver.getName_car()+", "+ driver.getYear_car());}
            if (driver.getRating()!=null){rating_driver.setText(driver.getRating()+"");}
            if (driver.getRewiews()!=null){review_driver.setText(driver.getRewiews().size()+"");}
            //if (driver.)
            if (driver.getImage_path()!=null){
                Picasso.with(ctx).load(driver.getImage_path()+"").resize(70,70).into(circleImageView);}
        }
    }

    private void init(View v) {
        back = (RelativeLayout) v.findViewById(R.id.back);

        tv_date = (TextView) v.findViewById(R.id.tv_date);
        review_driver_c = (TextView) v.findViewById(R.id.review_driver_c);
        d_tv_from = (TextView) v.findViewById(R.id.d_tv_from);
        d_tv_to = (TextView) v.findViewById(R.id.d_tv_to);
        d_tv_about = (TextView) v.findViewById(R.id.d_tv_about);
        places = (TextView) v.findViewById(R.id.places);

        name_driver_and_year = (TextView) v.findViewById(R.id.name_driver_and_year_c);
        car = (TextView) v.findViewById(R.id.car_c);
        rating_driver = (TextView) v.findViewById(R.id.rating_driver_c);
        review_driver = (TextView) v.findViewById(R.id.review_driver_c);
        finish_travels = (TextView) v.findViewById(R.id.finish_travels);

        circleImageView = (CircleImageView) v.findViewById(R.id.circleImageView_c);
        add_to_company = (ImageView) v.findViewById(R.id.add_to_company);
        if (companion==null){
            add_to_company.setVisibility(View.INVISIBLE);
        }
        number = (TextView) v.findViewById(R.id.number_c);

        changeFonts(tv_date,review_driver,review_driver_c,d_tv_about,d_tv_from,d_tv_to,places,name_driver_and_year,
                car,rating_driver,review_driver,finish_travels);
    }

    private void changeFonts(TextView tv_date, TextView review_driver, TextView review_driver_c,
                             TextView d_tv_about, TextView d_tv_from, TextView d_tv_to, TextView places,
                             TextView name_driver_and_year, TextView car, TextView rating_driver,
                             TextView review_driver1, TextView finish_travels) {
    }

    public void clicker(final Travel t){
        add_to_company.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams mRparams = new RelativeLayout
                        .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final EditText myEditText = new EditText(ctx);

                myEditText.setLayoutParams(mRparams);
                new AlertDialog.Builder(ctx)
                        .setTitle("Поездка")
                        .setView(myEditText)
                        .setMessage("Отправиться с этим водителем? \n\n\n Предложите свою цену, p.:")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String cost = "0";
                                cost = myEditText.getText().toString();
                                //Log.d(VARIABLES_CLASS.LOG_TAG,"cost = " + cost);
                                //checkSingelton(t,cost);
                                addMeToTravel(t,cost);
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private boolean checkSingelton(final Travel t, final String cost) {

        FirebaseDatabase.getInstance().getReference().child("zayavki").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    //Log.d(VARIABLES_CLASS.LOG_TAG,"boolean = " + check);
                    if (data.getValue(Zayavka.class).getCompanion().equals(companion.getDate_create())){
                        check = false;
                    }
                }
                if (check){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check;
    }

    private void addMeToTravel(Travel t,String cost) {
        if (t.getCompanion()<t.getPlaces()){
            Zayavka z = new Zayavka(cost,new Date().getTime()+"",t.getDriver_create()+"",companion.getDate_create()+""
            ,t.getTime_create()+"");
            if(checkSingelton(t,cost)){
                FirebaseDatabase.getInstance().getReference().child("zayavki").child(z.getDateCreate()+"").setValue(z);
                int numPlacesResult = t.getPlaces()-1;
                FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create()+"").child("places")
                        .setValue(numPlacesResult);
            }else{
                Toast.makeText(ctx, "Вы уже подали заявку на данную поездку!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ctx, "Свободных мест больше нет!", Toast.LENGTH_SHORT).show();

        }
    }

    // товар по позиции
    Travel getProduct(int position) {
        return ((Travel) getItem(position));
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
