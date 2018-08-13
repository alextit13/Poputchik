package com.poputchic.android.views.registration;

import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

public class AnimationBackground {

    private ScrollView sv;
    private ImageView iv;

    public void animation(ScrollView scroll_reg,ImageView image_back_registration){
        sv = scroll_reg;
        iv = image_back_registration;
        scroll_reg.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                iv.setY(-(sv.getScrollY() / 20));
            }
        });
    }
}
