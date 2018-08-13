package com.poputchic.android.admin.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;

import java.util.ArrayList;

public class FragmentAllMessage extends Fragment{

    Context context;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listDates = new ArrayList<>();
    ListView listView;
    int user = 0;
    Button button;
    EditText editText;

    public void FragmentAllMessage(Context ctx){
        context = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, null);
        listView = view.findViewById(R.id.list_all_users);
        button = view.findViewById(R.id.btn_send_all_users);
        editText = view.findViewById(R.id.et_all_user_messages);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        takeUsers();
        return view;
    }

    private void send() {
        if (!editText.getText().toString().equals("")){
            for (int i = 0; i<list.size();i++){
                FirebaseDatabase.getInstance().getReference().child("message_admin").child(listDates.get(i))
                        .setValue(editText.getText().toString());
            }

            editText.setText("");
            Toast.makeText(context, "Сообщение отправлено", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show();
        }

    }

    private void takeUsers() {
        list.clear();
        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            list.add(data.getValue(Driver.class).getName());
                            listDates.add(data.getValue(Driver.class).getDate_create()+"");
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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            list.add(data.getValue(Companion.class).getName());
                            listDates.add(data.getValue(Companion.class).getDate_create());
                        }
                        completeList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void completeList() {
        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }
}
