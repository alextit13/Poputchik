package com.poputchic.android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.poputchic.interfaces.GettingDriver;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ZayavkaCompletedAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ZayavkaFromCompanion> objects;

    TextView name_companion,about_driver,phone_driver,travel_from,travel_to,cost;

    Companion companion;
    String number = "";
    GettingDriver gettingDriver = null;

    public ZayavkaCompletedAdapter(Context context, ArrayList<ZayavkaFromCompanion> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public ZayavkaFromCompanion getItem(int position) {
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
            view = lInflater.inflate(R.layout.complete_zayavka, parent, false);
        }

        ZayavkaFromCompanion z = getProduct(position);
        //Log.d(VARIABLES_CLASS.LOG_TAG,"p = " + position);

        Date curentDate = new Date(Long.parseLong(z.getDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");


        ((TextView) view.findViewById(R.id.tv_date)).setText(sdf.format(curentDate));
        ((TextView) view.findViewById(R.id.d_tv_from)).setText(z.getFrom_location());
        ((TextView) view.findViewById(R.id.d_tv_to)).setText(z.getTo_location());

        final View v = view;

        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(z.getDriver())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                number = dataSnapshot.getValue(Driver.class).getNumberPhone();
                                Picasso.with(ctx).load(dataSnapshot.getValue(Driver.class).getImage_path())
                                        .into((CircleImageView)v.findViewById(R.id.circleImageView_c));
                                ((TextView) v.findViewById(R.id.d_tv_about)).setText(number);
                                ((TextView) v.findViewById(R.id.car_c)).setText(dataSnapshot.getValue(Driver.class).getName_car()
                                +", " + dataSnapshot.getValue(Driver.class).getYear_car());
                                ((TextView) v.findViewById(R.id.name_driver_and_year_c)).setText(dataSnapshot.getValue(Driver.class).getName()
                                + ", " + dataSnapshot.getValue(Driver.class).getYear());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

        changeFonts(view);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        return v;
    }

    private void changeFonts(View view) {
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.name_driver_and_year_c));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.car_c));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.tv_date));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_from));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_to));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.places));
        FontsDriver.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_about));
    }

    // товар по позиции
    ZayavkaFromCompanion getProduct(int position) {
        return ((ZayavkaFromCompanion) getItem(position));
    }
}
