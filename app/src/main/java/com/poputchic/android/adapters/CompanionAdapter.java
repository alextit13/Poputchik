package com.poputchic.android.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CompanionAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Companion> objects;

    ImageView list_comp_image;
    TextView list_comp_name,list_comp_about,list_comp_years,list_comp_num_review;

    public CompanionAdapter(){

    }

    public CompanionAdapter(Context context, ArrayList<Companion> products) {
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
            view = lInflater.inflate(R.layout.item_main_list_companions, parent, false);
            init(view);

        }

        Companion c = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        String urlImage = "";
        if (c.getImage_path()==null){
            urlImage = "http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png";
        }else{
            urlImage = c.getImage_path();
        }
        Picasso.with(ctx).load(urlImage).into(list_comp_image);

        if (c.getName()!=null) list_comp_name.setText(c.getName());
        if (c.getAbout()!=null)list_comp_about.setText(c.getAbout());
        if (c.getYear()!=0)list_comp_years.setText(c.getYear()+"");
        if (c.getRewiews()!=null&&c.getRewiews().size()!=0){
            list_comp_num_review.setText(c.getRewiews().size()+"");
        }else{
            list_comp_num_review.setText("0");
        }

        return view;
    }

    private void init(View v) {
        list_comp_image = (ImageView) v.findViewById(R.id.list_comp_image);
        list_comp_name = (TextView) v.findViewById(R.id.list_comp_name);
        list_comp_about = (TextView) v.findViewById(R.id.list_comp_about);
        list_comp_years = (TextView) v.findViewById(R.id.list_comp_years);
        list_comp_num_review = (TextView) v.findViewById(R.id.list_comp_num_review);

    }

    // товар по позиции
    Companion getProduct(int position) {
        return ((Companion) getItem(position));
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}