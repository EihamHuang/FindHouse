package com.findhouse.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    /**
     * 获取context
     * @return
     */
    public static Context getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

}

