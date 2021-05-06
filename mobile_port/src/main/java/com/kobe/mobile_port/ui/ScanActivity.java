package com.kobe.mobile_port.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.rxbinding3.view.RxView;
import com.kobe.lib_base.BaseActivity;
import com.kobe.lib_base.DataBean;
import com.kobe.lib_zxing.zxing.activity.CaptureActivity;
import com.kobe.mobile_port.R;
import com.kobe.mobile_port.config.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class ScanActivity extends BaseActivity implements HomePresenter.HomeView {
    private TextView mTvScan;
    private ConstraintLayout mClInput;

    private TextInputEditText mEtInput;
    private TextView mTvConfirm;
    private HomePresenter homePresenter;
    private TextView mTvDeviceInfo;
    private ImageView mIvClose;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mTvScan = findViewById(R.id.tv_scan);
        mClInput = findViewById(R.id.cl_input);
        mTvDeviceInfo = findViewById(R.id.tv_device_info);
        mIvClose = findViewById(R.id.iv_close);

        RxView.clicks(mTvScan).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    if (!checkPermission(Manifest.permission.CAMERA)) {
                        String[] permissions = new String[]{Manifest.permission.CAMERA};
                        requestPermission(permissions, new OnPermissionsResultListener() {
                            @Override
                            public void OnSuccess() {
                                Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 0);
                            }

                            @Override
                            public void OnFail(List<String> failedPermissionList) {
                                Toast.makeText(ScanActivity.this, "扫描功能需要开启相机权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, 0);
                    }
                });

        homePresenter = new HomePresenter(this);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mEtInput = findViewById(R.id.et_input);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvConfirm.setEnabled(!TextUtils.isEmpty(editable.toString()));
            }
        });
        mTvConfirm.setOnClickListener(v -> homePresenter.sendTransmission(mEtInput.getEditableText().toString().trim()));
        mIvClose.setOnClickListener(v -> {
                    mEtInput.setText("");
                    mTvScan.setVisibility(View.VISIBLE);
                    mClInput.setVisibility(View.GONE);
                    homePresenter.sendTransmission("close");
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                try {
                    DataBean dataBean = new Gson().fromJson(result, DataBean.class);
                    if (!TextUtils.isEmpty(dataBean.cid)) {
                        mTvScan.setVisibility(View.GONE);
                        mClInput.setVisibility(View.VISIBLE);
                        Config.cid = dataBean.cid;
                        mTvDeviceInfo.setText("电视设备信息\n" + "生产商：" + dataBean.manufacturer + " " + "型号：" + dataBean.model);
                    }
                } catch (JsonSyntaxException exception) {
                    Toast.makeText(ScanActivity.this, "请正确扫描电视端的二维码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onNotificationSended(String msg) {

    }

    @Override
    public void onNotificationSendFailed(String msg) {

    }
}
