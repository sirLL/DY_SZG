package com.wareroom.versionchecklib.v2;

import android.content.Context;

import androidx.annotation.Nullable;

import com.wareroom.versionchecklib.core.http.AllenHttp;
import com.wareroom.versionchecklib.utils.AllenEventBusUtil;
import com.wareroom.versionchecklib.v2.builder.DownloadBuilder;
import com.wareroom.versionchecklib.v2.builder.RequestVersionBuilder;
import com.wareroom.versionchecklib.v2.builder.UIData;
import com.wareroom.versionchecklib.v2.eventbus.AllenEventType;

/**
 * Created by wareroom on 2018/1/12.
 */

public class AllenVersionChecker {

    //    public AllenVersionChecker() {
//        throw new RuntimeException("AllenVersionChecker can not be instantiated from outside");
//    }
    private AllenVersionChecker() {

    }

    public static AllenVersionChecker getInstance() {
        return AllenVersionCheckerHolder.allenVersionChecker;
    }

    private static class AllenVersionCheckerHolder {
        public static final AllenVersionChecker allenVersionChecker = new AllenVersionChecker();
    }

    @Deprecated
    public void cancelAllMission(Context context) {
        cancelAllMission();
    }

    public void cancelAllMission() {
        AllenHttp.getHttpClient().dispatcher().cancelAll();
//        Intent intent = new Intent(context.getApplicationContext(), VersionService.class);
//        context.getApplicationContext().stopService(intent);
        AllenEventBusUtil.sendEventBusStick(AllenEventType.CLOSE);
        AllenEventBusUtil.sendEventBusStick(AllenEventType.STOP_SERVICE);
    }

    /**
     * @param versionBundle developer should return version bundle ,to use when showing UI page,could be null
     * @return download builder for download setting
     */
    public DownloadBuilder downloadOnly(@Nullable UIData versionBundle) {
        return new DownloadBuilder(null, versionBundle);
    }

    /**
     * use request version function
     *
     * @return requestVersionBuilder
     */
    public RequestVersionBuilder requestVersion() {
        return new RequestVersionBuilder();
    }

}
