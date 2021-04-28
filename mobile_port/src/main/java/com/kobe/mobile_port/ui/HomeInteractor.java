package com.kobe.mobile_port.ui;

import com.kobe.lib_base.BaseInteractor;
import com.kobe.mobile_port.config.Config;
import com.kobe.mobile_port.net.RetrofitManager;
import com.kobe.mobile_port.net.request.LinkNotificationRequest;
import com.kobe.mobile_port.net.request.TransmissionRequest;
import com.kobe.mobile_port.net.response.NotificationResp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Time：2020-03-10 on 14:15.
 * Decription:.
 * Author:jimlee.
 */
public class HomeInteractor implements BaseInteractor {

    private final CompositeDisposable disposables;

    public HomeInteractor() {
        disposables = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    public void sendTransmission(String msg, final NotificationListener listener) {
        TransmissionRequest request = createTransmission(msg);
        disposables.add(RetrofitManager.sendTransmission(request).subscribe(new Consumer<NotificationResp>() {
            @Override
            public void accept(NotificationResp notificationResp)  {
                if (notificationResp.result.equals("ok")) {
                    listener.onSendNotificationSuccess("透传请求发送成功：当前cid " +
                            (notificationResp.status.equals("successed_online") ? "在线" : "离线"));
                } else {
                    listener.onSendNotificationSuccess(notificationResp.result);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                listener.onSendNotificationFailed(throwable.getMessage());
            }
        }));
    }


    private TransmissionRequest createTransmission(String msg) {
        LinkNotificationRequest.Message message = new LinkNotificationRequest.Message();
        message.appkey = Config.appKey;
        message.is_offline = false;
        message.msgtype = "transmission";

        TransmissionRequest.Transmission transmission = new TransmissionRequest.Transmission();
        transmission.transmission_type = false;

        transmission.transmission_content = msg;

        TransmissionRequest request = new TransmissionRequest();
        request.message = message;
        request.transmission = transmission;
        request.cid = Config.cid;
        request.requestid = createRequestId();

        return request;
    }

    private String createRequestId() {
        long r = (long) ((Math.random() + 1) * 1000);
        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

    public interface NotificationListener {
        void onSendNotificationSuccess(String msg);

        void onSendNotificationFailed(String msg);
    }


}
