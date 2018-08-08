package com.poputchic.android.activities.main_list;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.activities.DetailViewTravel;
import com.poputchic.android.activities.events.EventRefreshAdapter;
import com.poputchic.android.adapters.LZFC.LZFCAdapter;
import com.poputchic.android.adapters.TravelAdapter;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.classes.Data;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Travel;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.poputchic.android.find_fragments.FindFragment;
import com.poputchic.android.find_fragments.MessageFragment;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

public class MainListActivity extends Activity implements FindFragment.EditNameDialogListener {

    private ListView b_main_list;
    private ImageView /*b_menu_1, b_menu_2, b_menu_3, b_menu_4, b_menu_5,*/ b_iv_find,message_from_admin;
    private Driver driver;
    private ArrayList<Companion>listCompanions = new ArrayList<>();
    private Companion companion;
    private ArrayList<Travel> listTravesl;
    private AVLoadingIndicatorView main_list_progress_bar;
    private TravelAdapter adapter;
    private ArrayList listDrivers;
    private String city = "";
    private LZFCAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        init();
    }

    private void changeFont(TextView tv){
        // именение шрифтов вьюх
        FontsDriver.changeFontToComfort(this,tv);
    } // меняем шрифт вьюхи на главном экаране

    private void init() {
        changeFont((TextView)findViewById(R.id.main_list_toolbar));

        main_list_progress_bar = (AVLoadingIndicatorView) findViewById(R.id.main_list_progress_bar);
        main_list_progress_bar.setVisibility(View.VISIBLE);
        b_main_list = (ListView) findViewById(R.id.b_main_list);

        message_from_admin = (ImageView) findViewById(R.id.message_from_admin);
        b_iv_find = (ImageView) findViewById(R.id.b_iv_find);
        b_iv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFragment fFrag = new FindFragment();
                fFrag.FindFragment(MainListActivity.this);
                fFrag.setTargetFragment(fFrag,11);
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
                    ChangeRating cr = new ChangeRating(MainListActivity.this);
                    cr.addClicker();
                }
            }
        });
        message_from_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = "";
                if (companion!=null){
                     date = companion.getDate_create()+"";
                }else if (driver!=null){
                    date = driver.getDate_create()+"";
                }
                showDialogMessageFromAdmin(date);
            }
        });
    }

    private void showDialogMessageFromAdmin(String date) {
        MessageFragment fFrag = new MessageFragment();
        fFrag.FindFragment(MainListActivity.this,date);
        fFrag.show(getFragmentManager(),"fragMes");
    }

    private void clickPosition(int p) {
        Intent intent = new Intent(MainListActivity.this, DetailViewTravel.class);
        intent.putExtra("companion", companion);
        intent.putExtra("travel", listTravesl.get(p));
        startActivity(intent);
    }

    private void selectUser() {
        try {
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

    @Subscribe (threadMode = ThreadMode.ASYNC, sticky = true)
    public void onEventRefreshAdapter(EventRefreshAdapter eventRefreshAdapter){
        takeZayavkiFromCompanions();
    }

    private void takeZayavkiFromCompanions() {
        FirebaseDatabase.getInstance().getReference().child("zayavki_from_companoins")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<ZayavkaFromCompanion>list = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    if (new Date().getTime() < Long.parseLong(data.getValue(ZayavkaFromCompanion.class).getFrom_time())){
                                        list.add(data.getValue(ZayavkaFromCompanion.class));
                                    }
                                }

                                getListCompanions(list);
                                //completeAdapter(list);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
    }

    private void getListCompanions(final ArrayList<ZayavkaFromCompanion> list) {
        if (list==null||list.size()==0){
            // we are get empty list. All zayavki is not actual in this time
            completeAdapter(list,listCompanions);
            Toast.makeText(this, "Нет активных заявок", Toast.LENGTH_SHORT).show();
            main_list_progress_bar.setVisibility(View.INVISIBLE);
        }else{
            listCompanions.clear();
            for (int i = 0; i<list.size();i++){
                FirebaseDatabase.getInstance().getReference().child("users").child("companion").child(list.get(i).getCompanion()+"")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        listCompanions.add(dataSnapshot.getValue(Companion.class));
                                        if (listCompanions.size()==list.size()){
                                            completeAdapter(list,listCompanions);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
            }
        }
    }

    private void completeAdapter(ArrayList<ZayavkaFromCompanion> LZFC,ArrayList<Companion>listComp) {
        mAdapter = new LZFCAdapter(MainListActivity.this,LZFC,driver,listComp);
        b_main_list.setAdapter(mAdapter);
        main_list_progress_bar.setVisibility(View.INVISIBLE);
    }

    private void takeAndStartWithListTravels() {
        listTravesl = new ArrayList<>();
        listDrivers = new ArrayList<>();

        try {

            FirebaseDatabase.getInstance().getReference().child("travels")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listTravesl.clear();

                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                if (new Date().getTime() < Long.parseLong(data.getValue(Travel.class).getTime_from())){
                                    if (!city.equals("")&&data.getValue(Travel.class).getFrom().contains(city)){

                                        listTravesl.add(data.getValue(Travel.class));
                                    }else if (city.equals("")){

                                        listTravesl.add(data.getValue(Travel.class));
                                    }else if (city.equals("ВСЕ")){
                                        listTravesl.add(data.getValue(Travel.class));
                                    }
                                }

                            }
                            if (listTravesl.size()==0){
                                Toast.makeText(MainListActivity.this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
                                main_list_progress_bar.setVisibility(View.INVISIBLE);
                            }
                            takeDrivers();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }catch (Exception e){
            System.out.println("crash");
        }
    }

    private void takeDrivers() {
        listDrivers.clear();
        for (int i = 0; i<listTravesl.size();i++){
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child("drivers")
                    .child(listTravesl.get(i).getDriver_create()+"")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listDrivers.add(dataSnapshot.getValue(Driver.class));

                            //
                            main_list_progress_bar.setVisibility(View.INVISIBLE);
                            if (listDrivers.size() == listTravesl.size()){
                                //Log.d(VARIABLES_CLASS.LOG_TAG,"goIIIING = " + listDrivers.size());
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
        adapter = new TravelAdapter(this, LDR, companion,LDRDrivers);
        b_main_list.setAdapter(adapter);

    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                controller.myProfile(driver,companion);
                break;
            case R.id.b_menu_2:
                controller.exit();
                break;
            case R.id.b_menu_3:
                controller.addClick(driver,companion);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(driver,companion);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(driver,companion);
                break;
        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        city = inputText;
        if (driver!=null){
            takeZayavkiFromCompanions();
        }else if (companion!=null){
            takeAndStartWithListTravels();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
