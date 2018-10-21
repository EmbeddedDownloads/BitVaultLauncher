package com.bitvault.launcher.Control;

import android.app.Activity;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.AndroidAppUtils;


/**
 * Created by Anshuman on 10/4/2016.
 */

public class LoadHTMLData {
    /**
     * Activity Instance
     *
     * @param mActivity
     */
    private Activity mActivity;
    /**
     * Debugging TAG
     */
    private String TAG = LoadHTMLData.class.getSimpleName();
    /**
     * String Data
     */
    private String htmlContent = "";
    /**
     * Webview
     */
    private WebView mWebView;
    /**
     * Data text Size
     */
    private int contentTextSize = 22;

    /**
     * Constructor of this class
     *
     * @param mActivity
     */
    public LoadHTMLData(Activity mActivity, WebView mWebView, String htmlContent) {
        this.mActivity = mActivity;
        this.mWebView = mWebView;
        this.htmlContent = htmlContent;
        initLoadingData();
    }

    /**
     * initialized loading of data
     */
    private void initLoadingData() {
        AndroidAppUtils.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.loading), true);
        mWebView.getSettings();
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.getSettings().setDefaultFontSize(contentTextSize);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new CustomWebViewClient());
//        htmlContent = "<font color='white'>" + htmlContent + "</font>";
        mWebView.loadUrl("about:blank");
        mWebView.loadData(htmlContent, "text/html; charset=utf-8", "UTF-8");
    }

    /**
     * WebView Client to manage bookmark
     */
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AndroidAppUtils.hideProgressDialog();
        }
    }
}
