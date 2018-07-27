package com.poputchic.android.classes;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class Data{

    public Context context;

    public Data(Context ctx) {
        context = ctx;
    }

    public void saveSharedPreferenceCOMPANION(Companion c) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(VARIABLES_CLASS.FILENAME, MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();
            if (c==null){
                String json = gson.toJson("");
                bw.write(json);
            }else{
                String json = gson.toJson(c.getDate_create());
                bw.write(json);
            }
            // закрываем поток
            bw.close();
            //Log.d(MainActivity.LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSharedPreferenceDRIVER(Driver d) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput("user", MODE_PRIVATE)));
            // пишем данные
            Gson gson = new Gson();

            if (d==null){
                String json = gson.toJson("");
                bw.write(json);
            }else{
                String json = gson.toJson(d.getDate_create());
                bw.write(json);
            }

            // закрываем поток
            bw.close();
            //Log.d(MainActivity.LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCompanionData(){
        String companion = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.openFileInput(VARIABLES_CLASS.FILENAME)));
        } catch (FileNotFoundException e) {
            Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_0_1");
            e.printStackTrace();
        }
        String str = "";
        // читаем содержимое
        Gson gson = new Gson();
        try {
            while ((str = br.readLine()) != null) {
                Log.d(VARIABLES_CLASS.LOG_TAG,"str = " + str);
                try {
                    companion = gson.fromJson(str,String.class);
                }catch (Exception e){
                    //Log...
                    Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_1");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_1_1");
        }
        return companion;
    }

    public String getDriverData(){
        String driver = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.openFileInput(VARIABLES_CLASS.FILENAME)));
        } catch (FileNotFoundException e) {
            Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_0_2");
            e.printStackTrace();
        }
        String str = "";
        // читаем содержимое
        Gson gson = new Gson();
        try {
            while ((str = br.readLine()) != null) {
                //Log.d(VARIABLES_CLASS.LOG_TAG,"str = " + str);
                try {
                    driver = gson.fromJson(str,String.class);
                    //Log.d(VARIABLES_CLASS.LOG_TAG,"cm = " + driver);
                }catch (Exception e){
                    //Log...
                    Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_2");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(VARIABLES_CLASS.LOG_TAG,"EXCEPTION_2_2");
        }
        return driver;
    }
}
