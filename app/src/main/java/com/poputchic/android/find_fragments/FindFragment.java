package com.poputchic.android.find_fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.enums.Cities;

import java.util.Arrays;
import java.util.List;

public class FindFragment extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener{

    ListView listView;
    List<Cities> listCities;
    Context ctx;
    String city;

    public void FindFragment(Context context){
        ctx = context;
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        listCities = Arrays.asList(Cities.values());
        View v = inflater.inflate(R.layout.fragment_find, null);
        listView = (ListView) v.findViewById(R.id.list_cities_find);
        listView.setAdapter(new ArrayAdapter(ctx,android.R.layout.simple_dropdown_item_1line,listCities));



        v.findViewById(R.id.cancel_btn).setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = listCities.get(position).name();
                /*SharedPreferences sP = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sP.edit();
                editor.putString("city",city);
                editor.commit();*/

                //dismiss();
                onEditorAction(null,11,null);
            }
        });
        //getTargetFragment().onActivityResult(getTargetRequestCode(),);
        return v;
    }

    public void onClick(View v) {
        //Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        switch (v.getId()){
            case R.id.cancel_btn:
                //
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d(VARIABLES_CLASS.LOG_TAG, "event = " + event);
        EditNameDialogListener activity = (EditNameDialogListener) getActivity();
        activity.onFinishEditDialog(city);
        this.dismiss();
        return true;
    }
}
