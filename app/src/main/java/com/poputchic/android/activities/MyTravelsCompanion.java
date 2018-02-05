package com.poputchic.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTravelsCompanion extends Activity {

    private ImageView back_button_my_travels;
    private ListView list_my_z;
    private ArrayList<String>listStrings = new ArrayList<>();
    private ArrayList<Travel> listMyZ = new ArrayList<>();
    private Object list;
    private Companion companion;
    private int rating = 0;
    private int clickItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels_companion);
        init();
    }

    private void init() {
        companion = (Companion) getIntent().getSerializableExtra("companion");
        back_button_my_travels = (ImageView) findViewById(R.id.back_button_my_travels);
        list_my_z = (ListView) findViewById(R.id.list_my_z);
        getList();
    }

    public void getList() {
        FirebaseDatabase.getInstance().getReference().child("travels_complete_companion").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot data : dataSnapshot.getChildren()){

                            FirebaseDatabase.getInstance().getReference().child("travels_complete_companion").child(data.getKey()+"").addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                                if (d.getValue(String.class).equals(companion.getDate_create()+"")){
                                                    listStrings.add(data.getKey()+"");

                                                    getZayavkiFromFirebase(listStrings);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getZayavkiFromFirebase(ArrayList<String> LS) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"size = " + LS.size());
        if (!LS.isEmpty()){
            for (int i = 0; i<LS.size();i++){
                FirebaseDatabase.getInstance().getReference().child("travels").child(LS.get(i)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                listMyZ.add(dataSnapshot.getValue(Travel.class));
                                takeAnotherZayavki();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }
        }
    }

    private void takeAnotherZayavki() {
        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Travel travel = new Travel(0,
                            0,data.getValue(ZayavkaFromCompanion.class).getFrom_location()+"",
                            data.getValue(ZayavkaFromCompanion.class).getTo_location()+"",
                            "0","0",data.getValue(ZayavkaFromCompanion.class).getDriver()+""
                    ,"",data.getValue(ZayavkaFromCompanion.class).getDate()+"");
                    listMyZ.add(travel);
                }
                goToAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToAdapter() {
        if (!listMyZ.isEmpty()){
            TravelAdapter adapter = new TravelAdapter(MyTravelsCompanion.this,listMyZ,companion,1);
            list_my_z.setAdapter(adapter);
            list_my_z.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    clickItem = i;
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyTravelsCompanion.this);
// ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
                    dialogBuilder.setView(dialogView);

                    Button button_finish = (Button) dialogView.findViewById(R.id.finish);
                    Button cancel = (Button) dialogView.findViewById(R.id.cancel);

                    final int j = i;

                    final AlertDialog alertDialog = dialogBuilder.create();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    button_finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newDialogRatingAndRewiew(j);
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    private void newDialogRatingAndRewiew(final int i) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyTravelsCompanion.this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_and_review, null);
        dialogBuilder.setView(dialogView);

        final EditText review_RAR = (EditText) dialogView.findViewById(R.id.review_RAR);
        Button save_RAR = (Button) dialogView.findViewById(R.id.save_RAR);
        Button cancel_RAR = (Button) dialogView.findViewById(R.id.cancel_RAR);
        final TextView r_RAR_1 = (TextView) dialogView.findViewById(R.id.r_RAR_1);
        final TextView r_RAR_2 = (TextView) dialogView.findViewById(R.id.r_RAR_2);
        final TextView r_RAR_3 = (TextView) dialogView.findViewById(R.id.r_RAR_3);
        final TextView r_RAR_4 = (TextView) dialogView.findViewById(R.id.r_RAR_4);
        final TextView r_RAR_5 = (TextView) dialogView.findViewById(R.id.r_RAR_5);
        r_RAR_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 1;
                r_RAR_1.setBackgroundColor(Color.parseColor("#d7c100"));
                r_RAR_2.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_3.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_4.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_5.setBackgroundColor(Color.parseColor("#ffeb3b"));
            }
        });
        r_RAR_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 2;
                r_RAR_1.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_2.setBackgroundColor(Color.parseColor("#d7c100"));
                r_RAR_3.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_4.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_5.setBackgroundColor(Color.parseColor("#ffeb3b"));
            }
        });
        r_RAR_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 3;
                r_RAR_1.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_2.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_3.setBackgroundColor(Color.parseColor("#d7c100"));
                r_RAR_4.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_5.setBackgroundColor(Color.parseColor("#ffeb3b"));
            }
        });
        r_RAR_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 4;
                r_RAR_1.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_2.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_3.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_4.setBackgroundColor(Color.parseColor("#d7c100"));
                r_RAR_5.setBackgroundColor(Color.parseColor("#ffeb3b"));
            }
        });
        r_RAR_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 5;
                r_RAR_1.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_2.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_3.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_4.setBackgroundColor(Color.parseColor("#ffeb3b"));
                r_RAR_5.setBackgroundColor(Color.parseColor("#d7c100"));
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        cancel_RAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        save_RAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                save(review_RAR.getText().toString());
            }
        });
        alertDialog.show();
    }

    private void save(String review) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"listMyZ = " + listMyZ.size());
        Log.d(VARIABLES_CLASS.LOG_TAG,"clickItem = " + clickItem);
        FirebaseDatabase.getInstance().getReference().child("complete_travel").child(listMyZ.get(clickItem).getTime_create()+"")
                .setValue(listMyZ.get(clickItem));
        if (review!=null&&!review.equals("")){
            FirebaseDatabase.getInstance().getReference().child("reviews").child(listMyZ.get(clickItem).getDriver_create()+"")
                    .child(companion.getDate_create()+"")
                    .setValue(review);
        }

        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(listMyZ.get(clickItem).getDriver_create()+"")
                .child("rating")
                .setValue(rating+"");

        Toast.makeText(this, "Поездка завершена!", Toast.LENGTH_SHORT).show();
    }
}
