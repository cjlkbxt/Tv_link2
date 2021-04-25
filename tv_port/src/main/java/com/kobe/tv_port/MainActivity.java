package com.kobe.tv_port;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUrlEvent(UrlEvent urlEvent) {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(urlEvent.url);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQrCodeEvent(CidEvent cidEvent) {
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("cid_" + cidEvent.cid, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        // 设置图片
        mIvQrcode.setImageBitmap(qrCodeBitmap);
    }
}