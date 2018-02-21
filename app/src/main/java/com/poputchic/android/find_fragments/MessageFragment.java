package com.poputchic.android.find_fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.enums.Cities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageFragment extends DialogFragment{

    ListView listView;
    Context ctx;
    String message;
    String date = "";

    public void FindFragment(Context context,String d){
        ctx = context;
        date = d;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().setTitle("Title!");
        final View v = inflater.inflate(R.layout.fragment_message, null);

        FirebaseDatabase.getInstance().getReference().child("message_admin")
                .child(date+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message = dataSnapshot.getValue(String.class);
                if (message!=null&&
                        !message.equals(""))
                ((TextView)v.findViewById(R.id.message_text))
                        .setText(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((Button)v.findViewById(R.id.cancel_btn))
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                            }
                        }
                );
        //getTargetFragment().onActivityResult(getTargetRequestCode(),);
        return v;
    }
}
