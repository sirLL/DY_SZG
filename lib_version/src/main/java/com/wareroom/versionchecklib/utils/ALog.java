package com.wareroom.versionchecklib.utils;

import android.util.Log;

import com.wareroom.versionchecklib.core.AllenChecker;

/**
 * Created by wareroom on 2017/8/16.
 */

public class ALog {
    public static void e(String msg) {
        if (AllenChecker.isDebug()) {
            if (msg != null && !msg.isEmpty())
                Log.e("Allen Checker", msg);
        }
    }
}
