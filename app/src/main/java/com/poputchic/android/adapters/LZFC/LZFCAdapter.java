package com.poputchic.android.adapters.LZFC;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.models.companion.Companion;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.ZayavkaFromCompanion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LZFCAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private List<ZayavkaFromCompanion> objects;
    private List<Companion> listCompanions;
    private List<Driver> listDrivers;
    private Driver driver;

    public LZFCAdapter(Context context, List<ZayavkaFromCompanion> products,Driver d,List<Companion>listComp) {
        listCompanions = listComp;
        driver = d;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LZFCAdapter(Context context, List<ZayavkaFromCompanion> products,Driver d,List<Companion>listComp,List<Driver>l) {
        listCompanions = listComp;
        driver = d;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDrivers=l;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public ZayavkaFromCompanion getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // реализуем работу view holder
        HelperHolder holder; // объект холдера
        if (convertView==null){
            convertView = lInflater.inflate(R.layout.lzfcitem, parent, false);
            holder = new HelperHolder();
            init(holder,convertView, position); // инициализируем вьюхи
            clicker(holder,position); // инициализация кликеров
            convertView.setTag(holder); // меняем тэг вьюхи
        }else{
            holder = (HelperHolder) convertView.getTag();
        }
        changeViews(holder,position); // изменяем данные во вьюхах в зависимости от того, что пришло в данных
        return convertView;
    }

    private void clicker(HelperHolder holder, int position) {
        if (driver!=null){
            InitClickers iC = new InitClickers(this,ctx,position,objects,driver);
            iC.init(holder.button_ok_L,holder.button_ok_hide);
        }
    } // инициализируем и обрабатываем клики

    private void changeViews(HelperHolder holder, int position) {
        holder.about_driver_L.setText(listCompanions.get(position).getAbout());
        holder.phone_driver_L.setText(listCompanions.get(position).getPhone());
        holder.name_companion_L.setText(listCompanions.get(position).getName());
        holder.cost_L.setText(objects.get(position).getPrice());
        holder.travel_to_L.setText(objects.get(position).getTo_location());
        holder.travel_from_L.setText(objects.get(position).getFrom_location());
        if (driver==null){
            try {
                holder.driver_take_zayavka.setText(listDrivers.get(position).toString());
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        Picasso.with(ctx).load(listCompanions.get(position).getImage_path()+"").resize(100,100)
                .into(holder.iv_driver);
        changeFonts(holder,position);
    } // изменяем данные во вьюхах

    private void changeFonts(HelperHolder holder, int position) {
        /*FontsConteroller.changeFontToComfort(ctx,holder.about_driver_L);
        FontsConteroller.changeFontToComfort(ctx,holder.button_ok_hide);
        FontsConteroller.changeFontToComfort(ctx,holder.text_take_companion);
        FontsConteroller.changeFontToComfort(ctx,holder.cost_L);
        FontsConteroller.changeFontToComfort(ctx,holder.name_companion_L);
        FontsConteroller.changeFontToComfort(ctx,holder.phone_driver_L);
        FontsConteroller.changeFontToComfort(ctx,holder.travel_to_L);
        FontsConteroller.changeFontToComfort(ctx,holder.travel_from_L);*/
    }

    private void init(HelperHolder holder,View convertView, int position) {
        holder.about_driver_L = (TextView)convertView.findViewById(R.id.about_driver_L);
        holder.name_companion_L = (TextView)convertView.findViewById(R.id.name_companion_L);
        holder.phone_driver_L = (TextView)convertView.findViewById(R.id.phone_driver_L);
        holder.travel_from_L = (TextView)convertView.findViewById(R.id.travel_from_L);
        holder.travel_to_L = (TextView)convertView.findViewById(R.id.travel_to_L);
        holder.text_take_companion = (TextView)convertView.findViewById(R.id.text_take_companion);
        holder.cost_L = (TextView)convertView.findViewById(R.id.cost_L);
        holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.main_container_hint);
        holder.button_ok_L = (CardView) convertView.findViewById(R.id.button_ok_L);
        holder.button_ok_hide = (Button) convertView.findViewById(R.id.button_ok_hide);
        holder.driver_take_zayavka = (TextView) convertView.findViewById(R.id.driver_take_zayavka);
        if (driver==null){
            holder.button_ok_L.setVisibility(View.GONE);
            holder.button_ok_hide.setVisibility(View.GONE);
            holder.driver_take_zayavka.setVisibility(View.VISIBLE);
        }

        holder.iv_driver = (ImageView) convertView.findViewById(R.id.image_driver_L);
    } // инициализируем вьюхи

    class HelperHolder{
        ImageView iv_driver;
        TextView name_companion_L,about_driver_L,phone_driver_L,travel_from_L,travel_to_L,cost_L,text_take_companion,driver_take_zayavka;
        Button button_ok_hide;
        CardView button_ok_L;
        LinearLayout linearLayout;
    } // класс холдера
}
