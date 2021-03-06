package com.wareroom.versionchecklib.v2.callback;

import androidx.annotation.Nullable;

import com.wareroom.versionchecklib.v2.builder.DownloadBuilder;
import com.wareroom.versionchecklib.v2.builder.UIData;

/**
 * Created by wareroom on 2018/1/12.
 */

public interface RequestVersionListener  {
    /**
     * @param result the result string of request
     * @return developer should return version bundle ,to use when showing UI page,could be null
     */
    @Nullable
    UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result);

    void onRequestVersionFailure(String message);

}
