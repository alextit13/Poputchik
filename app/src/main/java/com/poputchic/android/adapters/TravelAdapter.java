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
import com.poputchic.android.classes.classes.Travel;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TravelAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Travel> objects;

    TextView tv_date,d_tv_from,d_tv_to,d_tv_about;

    public TravelAdapter(){

    }

    public TravelAdapter(Context context, ArrayList<Travel> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
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

        Travel t = getProduct(position);

        init(view);

        if (t.getTime_create()!=null){
            String d = "";
            Date date = new Date(Long.parseLong(t.getTime_create()));
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG,  Locale.getDefault());
            d = dateFormat.format(date);
            tv_date.setText(d);
        }
        if (t.getFrom()!=null){d_tv_from.setText(t.getFrom());}
        if (t.getTo()!=null){d_tv_to.setText(t.getTo());}
        if (t.getAbout_travel()!=null){d_tv_about.setText(t.getAbout_travel());}
        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        return view;
    }

    private void init(View v) {
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        d_tv_from = (TextView) v.findViewById(R.id.d_tv_from);
        d_tv_to = (TextView) v.findViewById(R.id.d_tv_to);
        d_tv_about = (TextView) v.findViewById(R.id.d_tv_about);
    }

    // товар по позиции
    Travel getProduct(int position) {
        return ((Travel) getItem(position));
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
