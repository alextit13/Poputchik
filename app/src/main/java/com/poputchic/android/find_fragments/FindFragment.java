package com.poputchic.android.find_fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.poputchic.android.R;
import com.poputchic.android.classes.enums.Cities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFragment extends DialogFragment implements View.OnClickListener{

    ListView listView;
    List<Cities> listCities;
    Context ctx;

    public void FindFragment(Context context){
        ctx = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        listCities = Arrays.asList(Cities.values());
        View v = inflater.inflate(R.layout.fragment_find, null);
        listView = (ListView) v.findViewById(R.id.list_cities_find);
        listView.setAdapter(new ArrayAdapter(ctx,android.R.layout.simple_dropdown_item_1line,listCities));
        v.findViewById(R.id.cancel_btn).setOnClickListener(this);
        v.findViewById(R.id.find_btn).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        //Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        switch (v.getId()){
            case R.id.cancel_btn:
                //
                dismiss();
                break;
            case R.id.find_btn:
                //
                break;
            default:
                break;
        }
    }
}
