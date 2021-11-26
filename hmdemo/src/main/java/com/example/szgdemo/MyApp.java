package com.example.szgdemo;

import android.app.Application;

import cn.dianyinhuoban.szg.DYHelper;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DYHelper.init(this);
    }
}
