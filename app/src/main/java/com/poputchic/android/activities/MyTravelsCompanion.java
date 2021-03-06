package com.poputchic.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.adapters.AdapterAceptUserAsCompanion;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.adapters.ZayavkaCompletedAdapter;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.models.VARIABLES_CLASS;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Zayavka;
import com.poputchic.android.models.ZayavkaFromCompanion;

import java.util.ArrayList;
import java.util.List;

public class MyTravelsCompanion extends Activity {

    private ImageView back_button_my_travels;
    private TextView logo_my_travels;
    private ListView list_my_z;
    private ArrayList<ZayavkaFromCompanion> listMyZ = new ArrayList<>();
    private Companion companion;
    private Driver driver;
    private int rating = 0;
    private int clickItem = 0;
    private ArrayList<ZayavkaFromCompanion>listZayavriFromCompanion = new ArrayList<>();
    private ArrayList<Zayavka>listZayavki = new ArrayList<>();
    private ArrayList<Companion>listCompanion = new ArrayList<>();
    private List<Object>listObj = new ArrayList<>();
    private List<ZayavkaFromCompanion>listSecondListZFC = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels_companion);
        init();
        changeFonts();
    }

    private void changeFonts() {
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.activity_my_travels_companion));
        //FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.logo_my_travels));
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                controller.myProfile(driver,companion);
                break;
            case R.id.b_menu_2:
                finish();
                //controller.exit();
                break;
            case R.id.b_menu_3:
                controller.addClick(driver,companion);
                break;
            case R.id.b_menu_4:
                //controller.imCompanoin(driver,companion);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(driver,companion);
                break;
        }
    }

    private void init() {
        logo_my_travels = (TextView) findViewById(R.id.logo_my_travels);
        try {
            driver = (Driver) getIntent().getSerializableExtra("driver");
            companion = (Companion) getIntent().getSerializableExtra("companion");
        }catch (Exception e){
            // Log ...
        }

        back_button_my_travels = (ImageView) findViewById(R.id.back_button_my_travels);
        list_my_z = (ListView) findViewById(R.id.list_my_z);

        //Log.d("log_user","d = " + driver.getDate_create() + ", c = " + companion.getDate_create());
        if (companion!=null){
            getList();
        }else if (driver!=null){
            logo_my_travels.setText("Ваши попутчики:");
            getListForDriver();
        }

    }

    private void getListForDriver() {

        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    if (data.getValue(ZayavkaFromCompanion.class).getDriver().equals(driver.getDate_create())){
                                        listZayavriFromCompanion.add(data.getValue(ZayavkaFromCompanion.class));
                                    }
                                }
                                //Log.d("log_user","list = " + listZayavriFromCompanion.size());
                                getZayavki();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    private void getZayavki() {
        FirebaseDatabase.getInstance().getReference().child("zayavki")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    if (data.getValue(Zayavka.class).getDriver().equals(driver.getDate_create())){
                                        listZayavki.add(data.getValue(Zayavka.class));
                                    }
                                }
                                //Log.d("log_user","list = " + listZayavriFromCompanion.size());
                                listObj.addAll(listMyZ);
                                listObj.addAll(listZayavki);
                                getCompanionList();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    private void getCompanionList() {
        for (int i = 0;i<listObj.size();i++){
            FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(((Zayavka)(listObj.get(i))).getCompanion())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listCompanion.add(dataSnapshot.getValue(Companion.class));
                            goToAdapter(listZayavriFromCompanion);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void getList() {
        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    if (data.getValue(ZayavkaFromCompanion.class).getCompanion().equals(companion.getDate_create())){
                                        listMyZ.add(data.getValue(ZayavkaFromCompanion.class));
                                    }
                                    //Log.d(VARIABLES_CLASS.LOG_TAG,"list size = " + listMyZ.size());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
        getSecondZayavkaList();
    }

    private void getSecondZayavkaList(){
        FirebaseDatabase.getInstance().getReference().child("zayavki").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Zayavka z = data.getValue(Zayavka.class);
                    if (z.getCompanion() != null) {
                        if (z.getCompanion().equals(companion.getDate_create())) {
                            listSecondListZFC.add(new ZayavkaFromCompanion(
                                    z.getDriver(),
                                    z.getDateCreate(),
                                    "",
                                    "",
                                    "",
                                    "",
                                    z.getCost(),
                                    z.getCompanion()
                            ));
                        }
                    }
                }
                listMyZ.addAll(listSecondListZFC);
                goToAdapter(listMyZ);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToAdapter(final List<ZayavkaFromCompanion>list) {
        AdapterAceptUserAsCompanion adapter = new AdapterAceptUserAsCompanion
                (MyTravelsCompanion.this,list);
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
                        if (driver==null){
                            newDialogRatingAndRewiew(j);
                        }else if (companion==null){
                            cancelTravel(j,listObj);
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void cancelTravel(int num_click, List<Object>L) {
        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .child(((Zayavka)(L.get(num_click))).getDateCreate())
                .removeValue()
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MyTravelsCompanion.this, "Поездка окончена", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
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
                save(review_RAR.getText().toString(),i);
            }
        });
        alertDialog.show();
    }

    private void save(String review, int i) {

        if (listMyZ.get(i).getFrom_time().equals("")) {
            FirebaseDatabase.getInstance().getReference().child("zayavki")
                    .child(listMyZ.get(i).getDate())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MyTravelsCompanion.this, "Поездка окончена!", Toast.LENGTH_SHORT).show();
                }
            });
        }


        FirebaseDatabase.getInstance().getReference().child("complete_travel").child(listMyZ.get(clickItem).getDate()+"")
                .setValue(listMyZ.get(clickItem));
        if (review!=null&&!review.equals("")){
            FirebaseDatabase.getInstance().getReference().child("reviews").child(listMyZ.get(clickItem).getDate()+"")
                    .child(companion.getDate_create()+"")
                    .setValue(review);
        }

        FirebaseDatabase.getInstance().getReference().child("complete_travels_with_zay_from_comp")
                .child(listMyZ.get(clickItem).getDate()).removeValue();

        FirebaseDatabase.getInstance().getReference().child("users").child("drivers").child(listMyZ.get(clickItem)
                .getDriver()+"").child("rating")
                .setValue(rating+"");

        Toast.makeText(this, "Поездка завершена!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
