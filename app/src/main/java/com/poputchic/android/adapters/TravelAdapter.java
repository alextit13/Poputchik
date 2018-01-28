package com.poputchic.android.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TravelAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Travel> objects;

    TextView tv_date,d_tv_from,d_tv_to,d_tv_about,places,
            name_driver_and_year,car,rating_driver,review_driver,finish_travels,number;
    CircleImageView circleImageView;
    ImageView add_to_company;
    Companion companion;

    public TravelAdapter(){

    }

    public TravelAdapter(Context context, ArrayList<Travel> products, Companion c) {
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
        if (view == null) {
            view = lInflater.inflate(R.layout.driver_list_item, parent, false);
        }

        Travel t = getProduct(position);

        init(view);

        if (t.getTime_create()!=null){
            String d = "";
            Date date = new Date(Long.parseLong(t.getTime_create()));
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG,  Locale.getDefault());
            d = dateFormat.format(date);
            tv_date.setText(d);
        }
        if (t.getFrom()!=null){d_tv_from.setText(t.getFrom());}
        if (t.getTo()!=null){d_tv_to.setText(t.getTo());}
        if (t.getAbout_travel()!=null){d_tv_about.setText(t.getAbout_travel());}
        if (t.getPlaces()!=0){places.setText("Свободно мест: "+t.getPlaces()+"/"+t.getPlaces());}

        takeDriver(t.getDriver_create());
        clicker(t);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        return view;
    }

    private void takeDriver(String dateCreate){
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(dateCreate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Driver driver = dataSnapshot.getValue(Driver.class);
                        if (driver.getName()!=null&&driver.getYear()!=0){name_driver_and_year.setText(driver.getName()+", "+driver.getYear());}
                        if (driver.getName_car()!=null&&driver.getYear_car()!=null){car.setText(driver.getName_car()+", "+ driver.getYear_car());}
                        if (driver.getRating()!=null){rating_driver.setText(driver.getRating()+"");}
                        if (driver.getRewiews()!=null){review_driver.setText(driver.getRewiews().size()+"");}
                        //if (driver.)
                        if (driver.getImage_path()!=null){
                            Picasso.with(ctx).load(driver.getImage_path()+"").resize(70,70).into(circleImageView);}
                        if (driver.getNumberPhone()!=null){number.setText(driver.getNumberPhone());}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void init(View v) {
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        d_tv_from = (TextView) v.findViewById(R.id.d_tv_from);
        d_tv_to = (TextView) v.findViewById(R.id.d_tv_to);
        d_tv_about = (TextView) v.findViewById(R.id.d_tv_about);
        places = (TextView) v.findViewById(R.id.places);

        name_driver_and_year = (TextView) v.findViewById(R.id.name_driver_and_year);
        car = (TextView) v.findViewById(R.id.car);
        rating_driver = (TextView) v.findViewById(R.id.rating_driver);
        review_driver = (TextView) v.findViewById(R.id.review_driver);
        finish_travels = (TextView) v.findViewById(R.id.finish_travels);

        circleImageView = (CircleImageView) v.findViewById(R.id.circleImageView);
        add_to_company = (ImageView) v.findViewById(R.id.add_to_company);
        if (companion==null){
            add_to_company.setVisibility(View.INVISIBLE);
        }
        number = (TextView) v.findViewById(R.id.number);
    }

    public void clicker(final Travel t){
        add_to_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Поездка")
                        .setMessage("Отправиться с этим водителем?")
                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //the user wants to leave - so dismiss the dialog and exit
                                addMeToTravel(t);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .show();
            }
        });
    }

    private void addMeToTravel(Travel t) {
        if (t.getCompanion()<t.getPlaces()){
            int companion = t.getCompanion();
            companion++;
            FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create()+"")
                    .child("companion").setValue(companion);
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
