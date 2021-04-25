package com.kobe.mobile_port.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.kobe.lib_base.BaseActivity;
import com.kobe.mobile_port.R;
import com.kobe.mobile_port.application.HomePresenter;

public class InputProjectAddressActivity extends BaseActivity implements HomePresenter.HomeView {

    private TextInputEditText mEtInput;
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ipaddress);
        homePresenter = new HomePresenter(this);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        mEtInput = findViewById(R.id.et_input);
        btnConfirm.setOnClickListener(v -> homePresenter.sendNotification(mEtInput.getEditableText().toString().trim()));
    }


    @Override
    public void onNotificationSended(String msg) {

    }

    @Override
    public void onNotificationSendFailed(String msg) {

    }
}
