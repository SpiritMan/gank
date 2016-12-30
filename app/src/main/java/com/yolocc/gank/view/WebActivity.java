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
import android.widget.Toast;

import com.yolocc.gank.R;
import com.yolocc.gank.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    public static final String URL = "url";
    public static final String DESC = "desc";

    private ActivityWebBinding activityWebBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        initToolbar(activityWebBinding.toolbar);
        activityWebBinding.title.setText(getIntent().getStringExtra(DESC));
        initWebView(activityWebBinding.webView, getIntent().getStringExtra(URL));
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
        Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
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
