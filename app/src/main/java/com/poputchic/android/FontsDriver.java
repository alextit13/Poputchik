package com.poputchic.android;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

public class FontsDriver {
    // КЛАСС КОТОРЫЙ УПРАВЛЯЕМ ШРИФТАМИ ВЬЮХ
    public static void changeFontToComfort(Context context, TextView t){
        Typeface tf = ResourcesCompat.getFont(context,R.font.comforta_light);
        t.setTypeface(tf);
    }
}
