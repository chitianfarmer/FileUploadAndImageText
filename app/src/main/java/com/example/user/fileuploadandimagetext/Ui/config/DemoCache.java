package com.example.user.fileuploadandimagetext.Ui.config;

import android.content.Context;

/**
 * Created by jezhee on 2/20/15.
 */
public class DemoCache {

    private static Context context;

    private static String account;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        DemoCache.account = account;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context.getApplicationContext();
    }
}
