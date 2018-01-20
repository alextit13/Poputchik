package com.poputchic.android.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Driver;

import java.util.ArrayList;

public class DriversAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Driver> objects;

    DriversAdapter(){

    }

    DriversAdapter(Context context, ArrayList<Driver> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.driver_list_item, parent, false);
        }

        Driver d = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tv_date)).setText("date");
        ((TextView) view.findViewById(R.id.d_tv_from)).setText("from");
        ((TextView) view.findViewById(R.id.d_tv_to)).setText("to");
        ((TextView) view.findViewById(R.id.d_tv_about)).setText("about");

        return view;
    }

    // товар по позиции
    Driver getProduct(int position) {
        return ((Driver) getItem(position));
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
