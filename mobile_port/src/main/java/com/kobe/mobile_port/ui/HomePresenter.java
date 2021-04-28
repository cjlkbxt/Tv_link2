package com.kobe.mobile_port.ui;

import com.kobe.lib_base.BaseInteractor;

/**
 * Time：2020-03-09 on 15:25.
 * Decription:.
 * Author:jimlee.
 */
public class HomePresenter implements HomeInteractor.NotificationListener {

    private final HomeView homeView;
    private final BaseInteractor notificationInteractor;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
        this.notificationInteractor = new HomeInteractor();
    }

    public void sendTransmission(String msg) {
        ((HomeInteractor) notificationInteractor).sendTransmission(msg, this);
    }

    @Override
    public void onSendNotificationSuccess(String msg) {
        if (homeView != null) {
            homeView.onNotificationSended(msg);
        }
    }

    @Override
    public void onSendNotificationFailed(String msg) {
        if (homeView != null) {
            homeView.onNotificationSendFailed(msg);
        }
    }

    public void onDestroy() {
        notificationInteractor.onDestroy();
    }

    public interface HomeView {
        void onNotificationSended(String msg);

        void onNotificationSendFailed(String msg);

    }
}
