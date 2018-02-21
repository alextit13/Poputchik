package com.poputchic.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class LZFCAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ZayavkaFromCompanion> objects;
    ArrayList<Companion> listCompanions;
    Companion companion;

    ImageView image_driver_L;
    TextView name_companion_L,about_driver_L,phone_driver_L,travel_from_L,travel_to_L,cost_L;
    Button button_ok_L;
    Driver driver;
    LinearLayout linearLayout;
    int numZ = 0;

    public LZFCAdapter(){

    }

    public LZFCAdapter(Context context, ArrayList<ZayavkaFromCompanion> products,Driver d,ArrayList<Companion>listComp) {
        listCompanions = listComp;
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
            view = lInflater.inflate(R.layout.lzfcitem, parent, false);
            init(view,position);
        }

        travel_from_L.setText(objects.get(position).getFrom_location());
        travel_to_L.setText(objects.get(position).getTo_location());
        cost_L.setText(objects.get(position).getPrice());

        name_companion_L.setText(listCompanions.get(position).getName());
        about_driver_L.setText(listCompanions.get(position).getAbout());
        phone_driver_L.setText(listCompanions.get(position).getPhone());

        Picasso.with(ctx).load(listCompanions.get(position).getImage_path()+"").into(image_driver_L);

        return view;
    }

    private void init(final View new_view, final int pos) {
        image_driver_L = (ImageView) new_view.findViewById(R.id.image_driver_L);

        name_companion_L = (TextView) new_view.findViewById(R.id.name_companion_L);
        about_driver_L = (TextView) new_view.findViewById(R.id.about_driver_L);
        phone_driver_L = (TextView) new_view.findViewById(R.id.phone_driver_L);
        travel_from_L = (TextView) new_view.findViewById(R.id.travel_from_L);
        travel_to_L = (TextView) new_view.findViewById(R.id.travel_to_L);
        cost_L = (TextView) new_view.findViewById(R.id.cost_L);
        linearLayout = (LinearLayout)new_view.findViewById(R.id.main_container_hint);

        button_ok_L = (Button) new_view.findViewById(R.id.button_ok_L);
        button_ok_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                        .addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()){
                                            if (data.getValue(Zayavka.class).getDriver().equals(driver.getDate_create())){
                                                numZ++;
                                            }
                                        }
                                        if (numZ<2){
                                            objects.get(pos).setDriver(driver.getDate_create()+"");
                                            FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp").child(objects.get(pos).getDate()+"")
                                                    .setValue(objects.get(pos));
                                            FirebaseDatabase.getInstance().getReference().child("zayavki_from_companoins").child(objects.get(pos).getDate()+"")
                                                    .removeValue()
                                                    .addOnCompleteListener(
                                                            new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(ctx, "Вы одобрили попутчика!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                    );
                                            //Log.d(VARIABLES_CLASS.LOG_TAG,"pos = " + pos);
                                        }else{
                                            Toast.makeText(ctx, "У вас максимальное количество одобренных заявок", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
            }
        });
        ((Button)new_view.findViewById(R.id.button_ok_hide))
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                objects.remove(pos);
                                notifyDataSetChanged();
                                Toast.makeText(ctx, "Заявка скрыта", Toast.LENGTH_SHORT).show();
                                //new_view.setVisibility(View.INVISIBLE);
                            }
                        }
                );
    }
}
