package com.kobe.tv_port.service;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.kobe.tv_port.event.CidEvent;
import com.kobe.tv_port.event.UrlEvent;

import org.greenrobot.eventbus.EventBus;

public class PushIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        CidEvent cidEvent = new CidEvent();
        cidEvent.cid = s;
        EventBus.getDefault().post(cidEvent);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        UrlEvent urlEvent = new UrlEvent();
        urlEvent.url = gtNotificationMessage.getContent();
        EventBus.getDefault().post(urlEvent);
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }
}
