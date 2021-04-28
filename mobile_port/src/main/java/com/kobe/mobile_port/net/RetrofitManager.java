package com.kobe.mobile_port.net;

import com.kobe.mobile_port.config.Config;
import com.kobe.mobile_port.net.NetApi;
import com.kobe.mobile_port.net.request.AuthRequest;
import com.kobe.mobile_port.net.request.LinkNotificationRequest;
import com.kobe.mobile_port.net.request.TransmissionRequest;
import com.kobe.mobile_port.net.response.AuthResp;
import com.kobe.mobile_port.net.response.NotificationResp;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Time：2020-03-09 on 15:36.
 * Decription:.
 * Author:jimlee.
 */
public class RetrofitManager {

    private static final NetApi netApi;

    static {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new NetInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://restapi.getui.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        netApi = retrofit.create(NetApi.class);
    }

    public static Observable<NotificationResp> sendTransmission(TransmissionRequest request) {
        return netApi.sendTransmission(Config.authToken, Config.appId, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AuthResp> auth(AuthRequest authRequest) {
        return netApi.auth(Config.appId, authRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
