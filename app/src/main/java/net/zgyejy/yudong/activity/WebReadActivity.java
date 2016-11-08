package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Article;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebReadActivity extends MyBaseActivity {
    private Article article;

    @BindView(R.id.web_progressBar)
    ProgressBar webProgressBar;
    @BindView(R.id.myWebView)
    WebView myWebView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_read);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");

        webSet();//网页设置
    }

    private void webSet() {
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);//设置支持JavaScript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);//允许访问文件
        webSettings.setBuiltInZoomControls(true);//设置显示缩放按钮
        webSettings.setSupportZoom(true);//支持缩放

        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebChromeClient client = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webProgressBar.setProgress(newProgress);
                if (newProgress >= 100)
                    webProgressBar.setVisibility(View.GONE);
            }
        };
        myWebView.setWebChromeClient(client);

        /**
         * 用于WebView显示图片，可使用这个参数设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS:适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        myWebView.loadUrl(url);
    }

    @OnClick(R.id.iv_web_back)
    public void onClick() {
        finish();
    }
}
