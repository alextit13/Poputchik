package com.poputchic.android.adapters.LZFC;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.classes.VARIABLES_CLASS;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Zayavka;
import com.poputchic.android.classes.classes.ZayavkaFromCompanion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class LZFCAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<ZayavkaFromCompanion> objects;
    private ArrayList<Companion> listCompanions;
    private Driver driver;

    public LZFCAdapter(Context context, ArrayList<ZayavkaFromCompanion> products,Driver d,ArrayList<Companion>listComp) {
        listCompanions = listComp;
        driver = d;
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        InitClickers iC = new InitClickers(this,ctx,position,objects,driver);
        iC.init(holder.button_ok_L,holder.button_ok_hide);
    } // инициализируем и обрабатываем клики

    private void changeViews(HelperHolder holder, int position) {
        holder.about_driver_L.setText(listCompanions.get(position).getAbout());
        holder.phone_driver_L.setText(listCompanions.get(position).getPhone());
        holder.name_companion_L.setText(listCompanions.get(position).getName());
        holder.cost_L.setText(objects.get(position).getPrice());
        holder.travel_to_L.setText(objects.get(position).getTo_location());
        holder.travel_from_L.setText(objects.get(position).getFrom_location());
        Picasso.with(ctx).load(listCompanions.get(position).getImage_path()+"").into(holder.iv_driver);
        changeFonts(holder,position);
    } // изменяем данные во вьюхах

    private void changeFonts(HelperHolder holder, int position) {
        FontsDriver.changeFontToComfort(ctx,holder.about_driver_L);
        FontsDriver.changeFontToComfort(ctx,holder.button_ok_hide);
        FontsDriver.changeFontToComfort(ctx,holder.button_ok_L);
        FontsDriver.changeFontToComfort(ctx,holder.cost_L);
        FontsDriver.changeFontToComfort(ctx,holder.name_companion_L);
        FontsDriver.changeFontToComfort(ctx,holder.phone_driver_L);
        FontsDriver.changeFontToComfort(ctx,holder.travel_to_L);
    }

    private void init(HelperHolder holder,View convertView, int position) {
        holder.about_driver_L = (TextView)convertView.findViewById(R.id.about_driver_L);
        holder.name_companion_L = (TextView)convertView.findViewById(R.id.name_companion_L);
        holder.phone_driver_L = (TextView)convertView.findViewById(R.id.phone_driver_L);
        holder.travel_from_L = (TextView)convertView.findViewById(R.id.travel_from_L);
        holder.travel_to_L = (TextView)convertView.findViewById(R.id.travel_to_L);
        holder.cost_L = (TextView)convertView.findViewById(R.id.cost_L);
        holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.main_container_hint);
        holder.button_ok_L = (Button) convertView.findViewById(R.id.button_ok_L);
        holder.button_ok_hide = (Button) convertView.findViewById(R.id.button_ok_hide);
        holder.iv_driver = (ImageView) convertView.findViewById(R.id.image_driver_L);
    } // инициализируем вьюхи

    class HelperHolder{
        ImageView iv_driver;
        TextView name_companion_L,about_driver_L,phone_driver_L,travel_from_L,travel_to_L,cost_L;
        Button button_ok_L,button_ok_hide;
        LinearLayout linearLayout;
    } // класс холдера
}
