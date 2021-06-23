package com.example.xiaojingw.base;

import android.app.Application;
import android.content.Context;

import com.example.xiaojingw.utils.RetrofitManager;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
    }

    public static Context getContext(){
        return context;
    }
}
