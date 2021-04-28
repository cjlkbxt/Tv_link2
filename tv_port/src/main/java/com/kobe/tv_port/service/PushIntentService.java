package com.kobe.tv_port.service;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.kobe.lib_base.SpManager;
import com.kobe.tv_port.event.CidEvent;
import com.kobe.tv_port.event.UrlEvent;

import org.greenrobot.eventbus.EventBus;

public class PushIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        SpManager.getInstance(context).put("cid", s);
        CidEvent cidEvent = new CidEvent();
        EventBus.getDefault().post(cidEvent);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        UrlEvent urlEvent = new UrlEvent();
        urlEvent.url = new String(gtTransmitMessage.getPayload());
        EventBus.getDefault().post(urlEvent);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }
}
