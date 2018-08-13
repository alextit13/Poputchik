package com.poputchic.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

public class ZayavkaCompletedAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    List<ZayavkaFromCompanion> objects;
    List<Companion>listCompanion = new ArrayList<>();
    List<Object>listZayavka = new ArrayList<>();

    TextView name_companion,about_driver,phone_driver,travel_from,travel_to,cost;

    Companion companion;
    String number = "";
    GettingDriver gettingDriver = null;

    public ZayavkaCompletedAdapter(Context context, List<ZayavkaFromCompanion> products,List<Companion>l,List<Object>lz) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listCompanion=l;
        listZayavka=lz;
    }

    @Override
    public int getCount() {
        return listZayavka.size();
    }

    @Override
    public Object getItem(int position) {
        return listZayavka.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        convertView = lInflater.inflate(R.layout.complete_zayavka, parent, false);

        Zayavka z = (Zayavka) getItem(position);
        //Log.d(VARIABLES_CLASS.LOG_TAG,"p = " + position);

        Date curentDate = new Date(Long.parseLong(z.getDateCreate()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");


       // ((TextView) convertView.findViewById(R.id.tv_date)).setText(sdf.format(curentDate));
        ((TextView) convertView.findViewById(R.id.tv_date)).setText(z.getCost() + "Р. ");
        //((ImageView) convertView.findViewById(R.id.to_image)).setVisibility(View.GONE);

        //((TextView) convertView.findViewById(R.id.d_tv_to)).setText(z.getTravel());

        final View v = convertView;

        Picasso.with(ctx).load(listCompanion.get(position).getImage_path())
                .into((CircleImageView)v.findViewById(R.id.circleImageView_c));
        //((TextView) v.findViewById(R.id.car_c)).setText(number);
        /*((TextView) v.findViewById(R.id.car_c)).setText(dataSnapshot.getValue(Driver.class).getName_car()
                +", " + dataSnapshot.getValue(Driver.class).getYear_car());*/
        ((TextView) v.findViewById(R.id.name_driver_and_year_c)).setText(listCompanion.get(position).getName());
        ((TextView) v.findViewById(R.id.car_c)).setText(listCompanion.get(position).getPhone());

        /*FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(z.getDriver())
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
                );*/

        changeFonts(convertView);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        return v;
    }

    private void changeFonts(View view) {
        /*FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.name_driver_and_year_c));
        FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.car_c));
        FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.tv_date));*/
        //FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_from));
        /*FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_to));
        FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.places));
        FontsConteroller.changeFontToComfort(ctx,(TextView) view.findViewById(R.id.d_tv_about));*/
    }

    // товар по позици
}
