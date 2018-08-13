package com.poputchic.android.models.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.poputchic.android.R;

import java.util.List;

public class FontsConteroller implements IFontsController{

    private Context context;
    private Typeface tf;

    public FontsConteroller(Context context) {
        this.context = context;
        tf = ResourcesCompat.getFont(context, R.font.comforta_light);
    }

    // КЛАСС КОТОРЫЙ УПРАВЛЯЕМ ШРИФТАМИ ВЬЮХ
    public void changeFontToComfort(TextView t){
        t.setTypeface(tf);
    }

    @Override
    public void changeFontToComfortButton(Button t){
        t.setTypeface(tf);
    }

    @Override
    public void changeAllListFontsInViews(TextView... viewsExtendetListView) {
        for (TextView tv : viewsExtendetListView) tv.setTypeface(tf);
    }

    @Override
    public void changeAllListFontsInViews(List<EditText> viewsExtendetListView) {
        for (EditText e : viewsExtendetListView) e.setTypeface(tf);
    }

    @Override
    public void changeAllListFontsInViewsTV(List<TextView> viewsExtendetListView) {
        for (TextView e : viewsExtendetListView) e.setTypeface(tf);
    }

}
