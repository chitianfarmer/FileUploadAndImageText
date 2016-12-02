package com.example.user.fileuploadandimagetext.demo;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.fileuploadandimagetext.Ui.config.DemoCache;

public class MyApplication extends Application {
    private static Context context = null;
    public static RequestQueue queue;//请求队列
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void onCreate() {
        super.onCreate();
        context = this;
        DemoCache.setContext(this);
        //请求
        queue = Volley.newRequestQueue(this);
    }
    /**
     * 获取请求队列
     * @return
     */
    public static RequestQueue getHttpQueue() {
        return queue;
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
