package com.kobe.mobile_port.application;

import android.app.Application;
import android.util.Log;

import com.kobe.mobile_port.config.Config;

public class MobilePortApplication extends Application implements AuthInteractor.IAuthFinished {
    @Override
    public void onCreate() {
        super.onCreate();
        startAuth();
    }

    private void startAuth() {
        AuthInteractor interactor = new AuthInteractor();
        interactor.checkTime(this);
        interactor.fetchAuthToken(this);
    }

    @Override
    public void onAuthFinished(String token) {
        Config.authToken = token;
    }

    @Override
    public void onAuthFailed(String msg) {

    }
}
