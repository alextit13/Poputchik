package com.poputchic.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.Zayavka;
import com.poputchic.android.models.ZayavkaFromCompanion;
import com.poputchic.interfaces.GettingDriver;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAceptUserAsCompanion extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    List<ZayavkaFromCompanion> objects;
    Companion companion;

    public AdapterAceptUserAsCompanion(Context context, List<ZayavkaFromCompanion> products) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        convertView = lInflater.inflate(R.layout.complete_zayavka, parent, false);

        ZayavkaFromCompanion z = (ZayavkaFromCompanion) getItem(position);
        //Log.d(VARIABLES_CLASS.LOG_TAG,"p = " + position);

        Date curentDate = new Date(Long.parseLong(z.getDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");

        Date curentDate_from = new Date(Long.parseLong(z.getFrom_time()));
        SimpleDateFormat sdf_from_hours = new SimpleDateFormat("HH");
        SimpleDateFormat sdf_from_minutes = new SimpleDateFormat("MM");
        SimpleDateFormat sdf_from = new SimpleDateFormat("dd MMMM");


         ((TextView) convertView.findViewById(R.id.tv_date)).setText(sdf.format(curentDate));
        ((TextView) convertView.findViewById(R.id.price_tv)).setText(z.getPrice() + " р.");
        ((TextView) convertView.findViewById(R.id.about_tv_travel)).setText(
                "Из: "+z.getFrom_location() + "\n" +
                "В: " +z.getTo_location() + "\n" +
                "Отправление: в " + sdf_from_hours.format(curentDate_from) + " ч. "
                        + sdf_from_minutes.format(curentDate_from) + " м. " + "\n"
                        + sdf_from.format(curentDate_from));

        //((ImageView) convertView.findViewById(R.id.to_image)).setVisibility(View.GONE);

        //((TextView) convertView.findViewById(R.id.d_tv_to)).setText(z.getTravel());

        final View v = convertView;


        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(getItem(position).getCompanion())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companion = dataSnapshot.getValue(Companion.class);
                        if (companion != null) {
                            Picasso.with(ctx).load(companion.getImage_path())
                                    .into((CircleImageView)v.findViewById(R.id.circleImageView_c));
                            ((TextView) v.findViewById(R.id.name_driver_and_year_c)).setText(companion.getName());
                            ((TextView) v.findViewById(R.id.car_c)).setText(companion.getPhone());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return v;
    }

    // товар по позици
}
