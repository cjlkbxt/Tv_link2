package com.kobe.tv_port;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kobe.lib_base.DataBean;
import com.kobe.lib_base.SpManager;
import com.kobe.lib_zxing.zxing.encoding.EncodingUtils;
import com.kobe.tv_port.event.CidEvent;
import com.kobe.tv_port.event.UrlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private ImageView mIvQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mIvQrcode = findViewById(R.id.iv_qrcode);

        mWebView = findViewById(R.id.wb_project);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        setQrCodeBitmap();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUrlEvent(UrlEvent urlEvent) {
        if (!TextUtils.equals(urlEvent.url, "close")) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(urlEvent.url);
        } else {
            mWebView.setVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQrCodeEvent(CidEvent cidEvent) {
        setQrCodeBitmap();
    }

    private void setQrCodeBitmap() {
        String cid = SpManager.getInstance(this).get("cid", "");
        DataBean dataBean = new DataBean();
        dataBean.manufacturer = Build.MANUFACTURER;
        dataBean.model = Build.MODEL;
        dataBean.cid = cid;
        if (!TextUtils.isEmpty(cid)) {

            Bitmap qrCodeBitmap = EncodingUtils.createQRCode(dataBean.toString(), 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            // 设置图片
            mIvQrcode.setImageBitmap(qrCodeBitmap);
        }
    }
}