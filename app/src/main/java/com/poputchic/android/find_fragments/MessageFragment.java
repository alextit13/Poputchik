package com.poputchic.android.find_fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;

public class MessageFragment extends DialogFragment{

    ListView listView;
    Context ctx;
    String message;
    String date = "";
    String messages = "";

    public void FindFragment(Context context,String d){
        ctx = context;
        date = d;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().setTitle("Title!");
        final View v = inflater.inflate(R.layout.fragment_message, null);

        FirebaseDatabase.getInstance().getReference().child("message_admin")
                .child(date+"").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messages += dataSnapshot.getValue(String.class) + "\n";
                ((TextView)v.findViewById(R.id.message_text))
                        .setText(messages);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("child_listener","onChildChanged = " + dataSnapshot + ", s = " + s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("child_listener","onChildRemoved = " + dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("child_listener","onChildMoved = " + dataSnapshot + ", s = " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("child_listener","databaseError = " + databaseError);
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
        changeFonts(v);
        return v;
    }

    private void changeFonts(View v) {
        /*FontsConteroller.changeFontToComfort(ctx,(TextView) v.findViewById(R.id.mesage_from_administration));
        FontsConteroller.changeFontToComfort(ctx,(TextView) v.findViewById(R.id.message_text));
        FontsConteroller.changeFontToComfort(ctx,(Button) v.findViewById(R.id.cancel_btn));*/
    }

    /***
     * message = dataSnapshot.getValue(String.class);
     if (message!=null&&
     !message.equals(""))
     ((TextView)v.findViewById(R.id.message_text))
     .setText(message);
     *
     * */

}
