package com.poputchic.android.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.poputchic.android.models.companion.Companion;

import java.util.ArrayList;

public class RewiewAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Companion> list;

    public RewiewAdapter() {

    }

    public RewiewAdapter(Context ctx, LayoutInflater lInflater, ArrayList<Companion> list) {
        this.ctx = ctx;
        this.lInflater = lInflater;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Companion getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
