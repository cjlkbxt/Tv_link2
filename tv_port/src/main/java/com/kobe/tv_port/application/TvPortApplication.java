package com.kobe.tv_port.application;

import android.app.Application;
import android.util.Log;

import com.igexin.sdk.BuildConfig;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

public class TvPortApplication extends Application {
    private static final String TAG = "cjl";
    @Override
    public void onCreate() {
        super.onCreate();
        initPushSdk();

    }

    private void initPushSdk() {
        PushManager.getInstance().initialize(this);
        if (BuildConfig.DEBUG) {
            //切勿在 release 版本上开启调试日志
            PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {

                @Override
                public void log(String s) {
                    Log.i(TAG, "log: ");

                }
            });
        }
    }
}
