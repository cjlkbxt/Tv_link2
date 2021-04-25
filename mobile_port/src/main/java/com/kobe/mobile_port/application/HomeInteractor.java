package com.kobe.mobile_port.application;

import android.util.Log;

import com.kobe.mobile_port.config.Config;
import com.kobe.mobile_port.net.RetrofitManager;
import com.kobe.mobile_port.net.request.LinkNotificationRequest;
import com.kobe.mobile_port.net.response.NotificationResp;

import java.text.SimpleDateFormat;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Time：2020-03-10 on 14:15.
 * Decription:.
 * Author:jimlee.
 */
public class HomeInteractor implements BaseInteractor {

    private final String TAG = this.getClass().getSimpleName();
    private CompositeDisposable disposables;
    private int payloadCount = 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public HomeInteractor() {
        disposables = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    public void sendNotification(String msg, NotificationListener listener) {
        LinkNotificationRequest request = createLinkNotificationRequest(msg);
        disposables.add(RetrofitManager.sendNotification(request).subscribe(new Consumer<NotificationResp>() {
            @Override
            public void accept(NotificationResp notificationResp) throws Exception {
                Log.i(TAG, "notification status = " + notificationResp.status);
                if (notificationResp.result.equals("ok")) {
                    listener.onSendNotificationSuccess("通知发送成功：cid" +
                            (notificationResp.status.equals("successed_online") ? "在线" : "离线"));
                } else {
                    listener.onSendNotificationSuccess(notificationResp.result);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "notification error = " + throwable.toString());
                listener.onSendNotificationFailed(throwable.getMessage());
            }
        }));
    }


    private LinkNotificationRequest createLinkNotificationRequest(String msg) {
        LinkNotificationRequest.Message message = new LinkNotificationRequest.Message();
        message.appkey = Config.appKey;
        message.is_offline = false;
        message.msgtype = "link";

        LinkNotificationRequest.Style style = new LinkNotificationRequest.Style();
        style.type = 0;
        style.text = msg;
        style.title = "这是一条点击跳转链接通知";
        style.logo = "push.png";
        style.big_style = 2;
        style.big_text = "长文本内容";
        style.is_clearable = true;
        style.is_vibrate = true;
        style.is_ring = true;


        LinkNotificationRequest.LinkTemplate linkTemplate = new LinkNotificationRequest.LinkTemplate();
        linkTemplate.url = "http://www.getui.com/";
        linkTemplate.style = style;


        LinkNotificationRequest request = new LinkNotificationRequest();
        request.message = message;
        request.link = linkTemplate;
        request.cid = Config.cid;
        request.requestid = createRequestId();

        return request;
    }

    private String createRequestId() {
        long r;
        r = (long) ((Math.random() + 1) * 1000);
        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

    public interface NotificationListener {
        void onSendNotificationSuccess(String msg);

        void onSendNotificationFailed(String msg);
    }


}
