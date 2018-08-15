package com.poputchic.android.models.fonts;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public interface IFontsController {
    void changeAllListFontsInViews(TextView... viewsExtendetListView);
    void changeAllListFontsInViews(List<EditText> viewsExtendetListView);
    void changeAllListFontsInViewsTV(List<TextView> viewsExtendetListView);
    void changeFontToComfortButton(Button b);
    void changeFontToComfortButton(List<Button>listButtons);

}
