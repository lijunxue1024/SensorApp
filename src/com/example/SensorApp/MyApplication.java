package com.example.SensorApp;

import android.app.Application;
import android.content.Context;

/**
 * Created by rabook on 2016/7/23.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate()
    {
        context = getApplicationContext();
    }

    public static Context getContext()
    {
        return context;
    }

}
