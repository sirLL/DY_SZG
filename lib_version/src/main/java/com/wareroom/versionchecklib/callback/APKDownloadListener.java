package com.wareroom.versionchecklib.callback;

import java.io.File;

/**
 * Created by wareroom on 2017/8/16.
 */

public interface APKDownloadListener {
     void onDownloading(int progress);
    void onDownloadSuccess(File file);
    void onDownloadFail();
}
