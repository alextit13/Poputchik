package com.poputchic.android.bottom_toolbar;

import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

public interface BottomToolbarListener {
    // описание методов класса контроллера нижнего тулбара
    void myProfile(Driver driver, Companion companion); // // мой профиль
    void exit(); // выход
    void addClick(Driver driver, Companion companion); // добавить
    void imCompanoin(Driver driver, Companion companion); // как попутчик
    void usersList(Driver driver, Companion companion); // пользователи
}
