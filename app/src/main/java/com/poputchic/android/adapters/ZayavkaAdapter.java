package com.poputchic.android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
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
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Travel;
import com.poputchic.android.models.Zayavka;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ZayavkaAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Zayavka> objects;

    TextView name_companion,about_driver,phone_driver,travel_from,travel_to,cost;

    CircleImageView image_driver;
    Button button_cancel,button_ok;

    Driver driver;
    Companion companion;
    Travel t;
    int num;
    Zayavka zayavka;

    public ZayavkaAdapter(){

    }

    public ZayavkaAdapter(Context context, ArrayList<Zayavka> products, Driver d) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        driver = d;
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
        convertView =lInflater.inflate(R.layout.item_zayavka, parent, false);

        Zayavka z = getProduct(position);
        init(convertView);
        getCompanion(z);



        clicker(z);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        return convertView;
    }

    private void completeViews(Zayavka z) {
        name_companion.setText(companion.getName());
        about_driver.setText(companion.getAbout());
        phone_driver.setText(companion.getPhone());
        Picasso.with(ctx).load(companion.getImage_path()).into(image_driver);
        getTravel(z);
    }

    private void getCompanion(final Zayavka z){
        FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(z.getCompanion()+"").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companion = dataSnapshot.getValue(Companion.class);
                        completeViews(z);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private Travel getTravel(final Zayavka z){
        FirebaseDatabase.getInstance().getReference().child("travels").child(z.getTravel()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        zayavka = z;
                        t = dataSnapshot.getValue(Travel.class);

                        travel_from.setText(t.getFrom());
                        travel_to.setText(t.getTo());
                        cost.setText(z.getCost()+" .p");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        return null;
    }

    private void init(View v) {
        name_companion = (TextView) v.findViewById(R.id.name_companion);
        about_driver = (TextView) v.findViewById(R.id.about_driver);
        phone_driver = (TextView) v.findViewById(R.id.phone_driver);
        travel_from = (TextView) v.findViewById(R.id.travel_from);
        travel_to = (TextView) v.findViewById(R.id.travel_to);
        cost = (TextView) v.findViewById(R.id.cost);


        image_driver = (CircleImageView) v.findViewById(R.id.image_driver);
        button_cancel = (Button) v.findViewById(R.id.button_cancel);
        button_ok = (Button) v.findViewById(R.id.button_ok);
        changeFonts(name_companion,about_driver,phone_driver,travel_from,travel_to,cost,button_cancel,button_ok);
    }

    private void changeFonts(TextView tv_date, TextView review_driver, TextView review_driver_c,
                             TextView d_tv_about, TextView d_tv_from, TextView d_tv_to, TextView places,
                             TextView name_driver_and_year) {
        /*FontsConteroller.changeFontToComfort(ctx,tv_date);
        FontsConteroller.changeFontToComfort(ctx,review_driver);
        FontsConteroller.changeFontToComfort(ctx,review_driver_c);
        FontsConteroller.changeFontToComfort(ctx,d_tv_about);
        FontsConteroller.changeFontToComfort(ctx,d_tv_from);
        FontsConteroller.changeFontToComfort(ctx,d_tv_to);
        FontsConteroller.changeFontToComfort(ctx,places);
        FontsConteroller.changeFontToComfort(ctx,name_driver_and_year);*/
    }

    public void clicker(final Zayavka z){
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancel
                new AlertDialog.Builder(ctx)
                        .setTitle("Отказ")
                        .setMessage("Вы действительно хотите отказать данному попутчику?")
                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //the user wants to leave - so dismiss the dialog and exit
                                FirebaseDatabase.getInstance().getReference().child("zayavki").child(z.getDateCreate()+"")
                                        .removeValue();
                                int num_c = t.getCompanion();
                                if (num_c!=0)num_c--;
                                FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create())
                                        .child("companion").setValue(num_c);
                                Toast.makeText(ctx, "Отказано!", Toast.LENGTH_SHORT).show();
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
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Отказ")
                        .setMessage("Вы действительно хотите взять этого попутчика в поездку?")
                        .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //the user wants to leave - so dismiss the dialog and exit
                                getNumCompanions(t);


                                Toast.makeText(ctx, "Кандидат одобрен!", Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .show();
            }
        });
    }

    private void getNumCompanions(final Travel t) {

        FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create()+"")
                .child("companion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue(Integer.class);
                completeCompanionToFirebase(num,t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void completeCompanionToFirebase(int n,Travel t) {
        n++;
        FirebaseDatabase.getInstance().getReference().child("travels").child(t.getTime_create()+"").child("companion").setValue(n);
        FirebaseDatabase.getInstance().getReference().child("travels_complete_companion")
                .child(t.getTime_create()+"")
                .child(n+"")
                .setValue(companion.getDate_create()+"");

        FirebaseDatabase.getInstance().getReference().child("zayavki").child(zayavka.getDateCreate()+"").removeValue();
    }

    // товар по позиции
    Zayavka getProduct(int position) {
        return ((Zayavka) getItem(position));
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
