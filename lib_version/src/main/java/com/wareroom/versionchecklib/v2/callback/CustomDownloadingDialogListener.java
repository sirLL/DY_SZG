package com.wareroom.versionchecklib.v2.callback;

import android.app.Dialog;
import android.content.Context;

import com.wareroom.versionchecklib.v2.builder.UIData;

/**
 * Created by wareroom on 2018/1/18.
 */

public interface CustomDownloadingDialogListener {
    Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle);

    void updateUI(Dialog dialog, int progress, UIData versionBundle);
}
