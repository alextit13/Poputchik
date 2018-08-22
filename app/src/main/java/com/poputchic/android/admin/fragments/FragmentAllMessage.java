package com.poputchic.android.admin.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.models.Cities;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentAllMessage extends Fragment {

    Context context;
    ListView listView;
    private Spinner citySpinnerAdminOneMessage;
    Button button;
    EditText editText;
    ArrayAdapter adapter;
    String city;
    private List<Driver>listDrivers = new ArrayList<>();
    private List<Companion>listCompanions = new ArrayList<>();
    private List<String>listDates = new ArrayList<>();
    boolean first = false;

    public void FragmentAllMessage(Context ctx) {
        context = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, null);
        listView = view.findViewById(R.id.list_all_users);
        button = view.findViewById(R.id.btn_send_all_users);
        editText = view.findViewById(R.id.et_all_user_messages);
        citySpinnerAdminOneMessage = view.findViewById(R.id.citySpinnerAdminOneMessage);
        completeSpinner();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        takeUsers();
        return view;
    }

    private void completeSpinner() {
        final List<Cities> cities = Arrays.asList(Cities.values());
        city = cities.get(0).name();
        citySpinnerAdminOneMessage.setAdapter(new ArrayAdapter<Cities>(getActivity(), android.R.layout.simple_list_item_1, cities));
        citySpinnerAdminOneMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cities.get(position).name();
                if (first) {
                    completeList();
                }
                first = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void send() {
        if (!editText.getText().toString().equals("")) {
            for (int i = 0; i < adapter.getCount(); i++) {
                FirebaseDatabase.getInstance().getReference().child("message_admin").child(listDates.get(i))
                        .setValue(editText.getText().toString());
            }

            editText.setText("");
            Toast.makeText(context, "Сообщение отправлено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show();
        }

    }

    private void takeUsers() {
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            listDrivers.add(d.getValue(Driver.class));
                        }
                        takeCompanions();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void takeCompanions() {

        FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            listCompanions.add(d.getValue(Companion.class));
                        }
                        completeList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void completeList() {
        listDates.clear();
        List<String>list = new ArrayList<>();
        for (Driver d : listDrivers) {
            if (d.getName() != null && (city.equals(d.getCity()) || city.equals("ВСЕ"))) {
                list.add(d.getName());
                listDates.add(d.getDate_create());
            }
        }
        for (Companion c : listCompanions) {
            if (c.getName() != null && (city.equals(c.getCity()) || city.equals("ВСЕ"))) {
                list.add(c.getName());
                listDates.add(c.getDate_create());
            }
        }



        adapter(list);

    }

    private void adapter (List<String>list) {
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
}
