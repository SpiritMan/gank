package com.yolocc.gank.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.yolocc.gank.Constants;
import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    public static final String URL = "url";
    public static final String DESC = "desc";
    public static final String IMAGE = "image";

    private ActivityWebBinding activityWebBinding;
    private String desc, url;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        initToolbar(activityWebBinding.toolbar);
        desc = getIntent().getStringExtra(DESC);
        url = getIntent().getStringExtra(URL);
        activityWebBinding.title.setText(desc);
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        initWebView(activityWebBinding.webView, url);
    }

    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initWebView(WebView webView, String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(true);
        webView.setWebChromeClient(new ChromeClient());
        webView.setWebViewClient(new GankClick());
        webView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (activityWebBinding.webView.canGoBack()) {
                        activityWebBinding.webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            activityWebBinding.progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                activityWebBinding.progressBar.setVisibility(View.GONE);
            } else {
                activityWebBinding.progressBar.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            activityWebBinding.title.setText(title);
        }
    }

    public void share(View view) {
        final ShareBottomDialogFragment shareBottomDialogFragment = new ShareBottomDialogFragment();
        shareBottomDialogFragment.show(getFragmentManager(), "shareFragment");
        shareBottomDialogFragment.setOnShareClickListener(new ShareBottomDialogFragment.OnShareClickListener() {
            @Override
            public void onTargetChose(int target) {
                switch (target) {
                    case ShareBottomDialogFragment.WECHAT_FRIEND_TARGET:
                        shareWechat(desc, url, 1);
                        break;
                    case ShareBottomDialogFragment.WECHAT_MOMENT_TARGET:
                        shareWechat(desc, url, 2);
                        break;
                }
            }
        });
    }

    /**
     * 分享图片到微信
     *
     * @param desc   网页描述
     * @param webUrl 网页链接
     * @param type   1表示分享给微信朋友，2表示分享到微信朋友圈
     */
    private void shareWechat(String desc, String webUrl, int type) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webUrl;
        WXMediaMessage mediaMessage = new WXMediaMessage();
        if (type == 1) {
            mediaMessage.title = "干货分享";
            mediaMessage.description = desc;
        } else {
            mediaMessage.title = desc;
        }
        mediaMessage.mediaObject = webpageObject;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = mediaMessage;
        req.scene = type == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class GankClick extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request != null) {
                view.loadUrl(request.getUrl().getPath());
            }
            return true;
        }
    }
}
