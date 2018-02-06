package com.poputchic.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.activities.person_rooms.PersonRoomCompanion;
import com.poputchic.android.activities.person_rooms.PersonRoomDriver;
import com.poputchic.android.adapters.LZFCAdapter;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.poputchic.android.find_fragments.FindFragment;

import java.util.ArrayList;

public class MainListActivity extends Activity {

    private ListView b_main_list;
    private ImageView b_menu_1, b_menu_2, b_menu_3, b_menu_4, b_menu_5, b_iv_find;
    private Driver driver;
    private Companion companion;

    private ArrayList<Travel> listTravesl;

    private ProgressBar main_list_progress_bar;
    private TravelAdapter adapter;
    private ArrayList listDrivers;
    private ArrayList<ZayavkaFromCompanion> listZayavkiFromCompanions = new ArrayList<>();
    int rating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        init();
    }

    private void init() {
        main_list_progress_bar = (ProgressBar) findViewById(R.id.main_list_progress_bar);
        main_list_progress_bar.setVisibility(View.VISIBLE);
        b_main_list = (ListView) findViewById(R.id.b_main_list);
        //Log.d(VARIABLES_CLASS.LOG_TAG,"click");

        b_menu_1 = (ImageView) findViewById(R.id.b_menu_1);
        b_menu_2 = (ImageView) findViewById(R.id.b_menu_2);
        b_menu_3 = (ImageView) findViewById(R.id.b_menu_3);
        b_menu_4 = (ImageView) findViewById(R.id.b_menu_4);
        b_menu_5 = (ImageView) findViewById(R.id.b_menu_5);
        b_iv_find = (ImageView) findViewById(R.id.b_iv_find);
        b_iv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFragment fFrag = new FindFragment();
                fFrag.FindFragment(MainListActivity.this);
                fFrag.show(getFragmentManager(),"frag");
            }
        });
        selectUser();
        b_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (companion != null) {
                    clickPosition(position);
                }if (driver != null){
                    addClicker();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"requestCode = " + requestCode + ", resultCode = " + resultCode);
    }

    private void clickPosition(int p) {
        Intent intent = new Intent(MainListActivity.this, DetailViewTravel.class);
        intent.putExtra("companion", companion);
        intent.putExtra("travel", listTravesl.get(p));
        startActivity(intent);
    }

    private void selectUser() {
        try {
            /*Data data = new Data(MainListActivity.this);
            driver = data.getDriverData();
            companion = data.getCompanionData();*/
            Intent intent = getIntent();
            driver = (Driver) intent.getSerializableExtra("driver");
            companion = (Companion) intent.getSerializableExtra("companion");
        } catch (Exception e) {
            //Log...
        }
        Data data = new Data(MainListActivity.this);
        if (driver != null) {
            // вошел водитель
            data.saveSharedPreferenceDRIVER(driver);
            takeZayavkiFromCompanions();
        } else if (companion != null) {
            // вошел пользователь
            data.saveSharedPreferenceCOMPANION(companion);
            takeAndStartWithListTravels();
        }
    }

    private void takeZayavkiFromCompanions() {
        FirebaseDatabase.getInstance().getReference().child("zayavki_from_companoins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listZayavkiFromCompanions.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            listZayavkiFromCompanions.add(data.getValue(ZayavkaFromCompanion.class));
                        }

                        main_list_progress_bar.setVisibility(View.INVISIBLE);
                        completeAdapter(listZayavkiFromCompanions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void completeAdapter(ArrayList<ZayavkaFromCompanion> LZFC) {
        LZFCAdapter adapter = new LZFCAdapter(MainListActivity.this,LZFC,driver);
        b_main_list.setAdapter(adapter);
    }

    private void addClicker() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainListActivity.this);
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

    private void takeAndStartWithListTravels() {
        listTravesl = new ArrayList<>();
        listDrivers = new ArrayList<>();
        try {
            FirebaseDatabase.getInstance().getReference().child("travels")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listTravesl.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                listTravesl.add(data.getValue(Travel.class));
                            }
                            takeDrivers();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }catch (Exception e){

        }
    }

    private void takeDrivers() {
        listDrivers.clear();
        for (int i = 0; i<listTravesl.size();i++){
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child("drivers")
                    .child(listTravesl.get(i).getDriver_create()+"")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listDrivers.add(dataSnapshot.getValue(Driver.class));

                            //
                            main_list_progress_bar.setVisibility(View.INVISIBLE);
                            if (listDrivers.size() == listTravesl.size()){
                                Log.d(VARIABLES_CLASS.LOG_TAG,"goIIIING = " + listDrivers.size());
                                travelsAdapter(listTravesl,listDrivers);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(VARIABLES_CLASS.LOG_TAG,"LDdatabaseErrorR");
                        }
                    });
        }
    }

    private void travelsAdapter(ArrayList<Travel> LDR,ArrayList<Driver> LDRDrivers) {
        Log.d(VARIABLES_CLASS.LOG_TAG,"LDR = "+LDR.size());
        adapter = new TravelAdapter(this, LDR, companion,LDRDrivers);
        b_main_list.setAdapter(adapter);

    }

    private void save(String s) {

    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.b_menu_1:
                // private room
                if (driver != null) {
                    Toast.makeText(MainListActivity.this, "Личный кабинет драйвер", Toast.LENGTH_SHORT).show();
                    goToPrivateRoomDriver(driver);
                } else if (companion != null) {
                    Toast.makeText(MainListActivity.this, "Личный кабинет компаньон", Toast.LENGTH_SHORT).show();
                    goToPrivateRoomCompanion(companion);
                }
                break;
            case R.id.b_menu_2:
                // exit
                exit();
                break;
            case R.id.b_menu_3:
                // add
                if (driver != null) {
                    addTravel();
                } else if (companion != null) {
                    addZayavka(companion);
                }
                break;
            case R.id.b_menu_4:
                if (driver != null) {
                    Intent intent = new Intent(MainListActivity.this, ZayavkiToMyTravels.class);
                    intent.putExtra("driver", driver);
                    startActivity(intent);
                } else if (companion != null) {
                    Intent intent = new Intent(MainListActivity.this, MyTravelsCompanion.class);
                    intent.putExtra("companion", companion);
                    startActivity(intent);
                }
                // ?
                break;
            case R.id.b_menu_5:
                // ?
                Intent intent = new Intent(MainListActivity.this,Reviews.class);
                if (driver!=null){
                    intent.putExtra("driver",driver);
                    startActivity(intent);
                }else if (companion!=null){
                    intent.putExtra("companion",companion);
                    startActivity(intent);
                }
                break;

        }
    }

    private void addZayavka(Companion C) {
        Intent intent = new Intent(MainListActivity.this, AddZayavka.class);
        intent.putExtra("companion", C);
        startActivity(intent);
    }

    private void goToPrivateRoomCompanion(Companion CO) {
        Intent intent = new Intent(MainListActivity.this, PersonRoomCompanion.class);
        intent.putExtra("companion", CO);
        startActivity(intent);
    }

    private void goToPrivateRoomDriver(Driver DR) {
        Intent intent = new Intent(MainListActivity.this, PersonRoomDriver.class);
        intent.putExtra("driver", DR);
        startActivity(intent);
    }

    private void addTravel() {
        Intent intent = new Intent(MainListActivity.this, AddTravel.class);
        intent.putExtra("driver", driver);
        startActivity(intent);
    }

    private void exit() {
        new AlertDialog.Builder(MainListActivity.this)
                .setTitle("Выход")
                .setMessage("Вы действительно хотите выйти?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //the user wants to leave - so dismiss the dialog and exit
                        Data data = new Data(MainListActivity.this);
                        data.saveSharedPreferenceDRIVER(null);
                        data.saveSharedPreferenceCOMPANION(null);


                        finish();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .show();
    }
}
