package com.example.szgdemo;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import cn.dianyinhuoban.szg.DYHelper;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "8dada2cf07", false);
        DYHelper.getInstance().init(this).setOnCheckVersionCallback(new DYHelper.OnCheckVersionCallback() {
            @Override
            public LinkedHashMap<String, String> getFetchVersionHeader() {
                //请求线上最新版本信息接口 header,若不需要返回null或者空集合
                return null;
            }

            @Override
            public LinkedHashMap<String, String> getFetchVersionParams() {
                //请求线上最新版本信息接口参数,若不需要返回null或者空集合
                return null;
            }

            @Override
            public DYHelper.RequestMethod getRequestMethod() {
                //请求线上最新版本信息接口  请求方式
                return DYHelper.RequestMethod.GET;
            }

            /**
             * @return 请求线上最新版本信息接口地址
             */
            @Override
            public String getRequestUrl() {
                return "";
//                return "http://szgapi.dyhm.shop/user_systemSetting.html";
            }

            /**
             * 请求线上最新版本信息接口 成功的回调
             * @param versionInfo 接口返回的信息
             * @return 下载地址    若返回null或者空字符串表示已经是最新版本不需要更新
             */
            @Override
            public String onCheckVersion(String versionInfo) {
                Log.d("MyApp", "onCheckVersion: " + versionInfo);
                String downloadURL = "";
                String onlineVersion = "";
                if (!TextUtils.isEmpty(versionInfo)) {
                    try {
                        JSONObject object = new JSONObject(versionInfo);
                        int code = object.optInt("code", 0);
                        if (code == 1) {
                            JSONArray array = object.optJSONArray("return");
                            if (array != null && array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject item = array.getJSONObject(i);
                                    if (item != null && "3".equals(item.getString("id"))) {
                                        downloadURL = item.optString("content", "");
                                    } else if (item != null && "1".equals(item.getString("id"))) {
                                        onlineVersion = item.optString("content", "");
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(onlineVersion)) {
                    int onlineV = Integer.parseInt(onlineVersion);
                    if (onlineV <= getVersionCode()) {
                        //已是最新版本不更新
                        downloadURL = "";
                    }
                }
                return downloadURL;
            }
        });
    }

    private int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;

    }
}
