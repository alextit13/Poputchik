package com.poputchic.android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class LZFCAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ZayavkaFromCompanion> objects;
    Companion companion;

    ImageView image_driver_L;
    TextView name_companion_L,about_driver_L,phone_driver_L,travel_from_L,travel_to_L,cost_L;
    Button button_cancel_L,button_ok_L;
    int pos;
    Driver driver;

    public LZFCAdapter(){

    }

    public LZFCAdapter(Context context, ArrayList<ZayavkaFromCompanion> products,Driver d) {
        driver = d;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = lInflater.inflate(R.layout.lzfcitem, parent, false);
            init(view);

        }

        pos = position;
        final ZayavkaFromCompanion ZFC = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(ZFC.getCompanion()+"")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companion = dataSnapshot.getValue(Companion.class);
                        Picasso.with(ctx).load(companion.getImage_path()+"").into(image_driver_L);

                        name_companion_L.setText(companion.getName()+"");
                        about_driver_L.setText(companion.getAbout()+"");
                        phone_driver_L.setText(companion.getPhone()+"");
                        travel_from_L.setText(ZFC.getFrom_location()+"");
                        travel_to_L.setText(ZFC.getTo_location()+"");
                        cost_L.setText(ZFC.getPrice()+"");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    private void init(View v) {
        image_driver_L = (ImageView) v.findViewById(R.id.image_driver_L);

        name_companion_L = (TextView) v.findViewById(R.id.name_companion_L);
        about_driver_L = (TextView) v.findViewById(R.id.about_driver_L);
        phone_driver_L = (TextView) v.findViewById(R.id.phone_driver_L);
        travel_from_L = (TextView) v.findViewById(R.id.travel_from_L);
        travel_to_L = (TextView) v.findViewById(R.id.travel_to_L);
        cost_L = (TextView) v.findViewById(R.id.cost_L);

        button_ok_L = (Button) v.findViewById(R.id.button_ok_L);
        button_ok_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.get(pos).setDriver(driver.getDate_create()+"");
                FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp").child(objects.get(pos).getDate()+"")
                        .setValue(objects.get(pos));
                Toast.makeText(ctx, "Вы одобрили попутчика!", Toast.LENGTH_SHORT).show();
                //Log.d(VARIABLES_CLASS.LOG_TAG,"pos = " + pos);
            }
        });
    }

    // товар по позиции
    ZayavkaFromCompanion getProduct(int position) {
        return ((ZayavkaFromCompanion) getItem(position));
    }
}
