package com.poputchic.android.adapters.LZFC;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.events.EventRefreshAdapter;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class InitClickers {
    private LZFCAdapter adapter;
    private Context context;
    private int numZ = 0;
    private int position = 0;
    private List<ZayavkaFromCompanion> objects;
    private Driver driver;


    public InitClickers(LZFCAdapter adapter, Context context, int position, List<ZayavkaFromCompanion> objects, Driver d) {
        this.adapter = adapter;
        this.context = context;
        this.position = position;
        this.objects = objects;
        this.driver = d;
        //EventBus.getDefault().register(this);
    }

    protected void init(View button_ok_L, View button_ok_hide){
        button_ok_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        numZ=0;
                                        for (DataSnapshot data : dataSnapshot.getChildren()){
                                            if (data.getValue(Zayavka.class).getDriver().equals(driver.getDate_create())){
                                                numZ++;
                                            }
                                        }
                                        numZ++;
                                        checkData();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
            }
        });
        button_ok_hide
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                objects.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "Заявка скрыта", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    public void checkData() {

        //EventBus.getDefault().postSticky(new EventRefreshAdapter(true));
        //Toast.makeText(context, "Вы одобрили попутчика!", Toast.LENGTH_SHORT).show();

        if (numZ<=2){
            objects.get(position).setDriver(driver.getDate_create()+"");
            FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp").child(objects.get(position).getDate()+"")
                    .setValue(objects.get(position));
            FirebaseDatabase.getInstance().getReference().child("zayavki_from_companoins").child(objects.get(position).getDate()+"")
                    .removeValue()
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    EventBus.getDefault().postSticky(new EventRefreshAdapter(true));
                                    Toast.makeText(context, "Вы одобрили попутчика!", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
            //Log.d(VARIABLES_CLASS.LOG_TAG,"pos = " + pos);
        }else{
            Toast.makeText(context, "У вас максимальное количество одобренных заявок", Toast.LENGTH_SHORT).show();
        }
    }
}
