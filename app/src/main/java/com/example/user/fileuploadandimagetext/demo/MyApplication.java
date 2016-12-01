package com.example.user.fileuploadandimagetext.demo;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context = null;
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        DemoCache.setContext(this);
    }
    public static Context getContext(){
        return context;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
