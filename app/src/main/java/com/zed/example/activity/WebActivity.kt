package com.zed.example.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zed.common.util.LogUtils
import com.zed.example.R
import com.zed.example.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Created by zed on 2018/4/4.
 */
class WebActivity : BaseActivity() {

    override fun setLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        val url = intent.getStringExtra(url)
        val title = intent.getStringExtra(WebActivity.title)
        LogUtils.i(TAG, "网址是:" + url)
        appTitle.setTitleLeftTxt()
        appTitle.setTitle(title)

        setStatusBarColor(R.color.color_d53434)
        appTitle.getTvTitle().setTextColor(getAppColor(R.color.color_ffffff))
        appTitle.setBackgroundColor(getAppColor(R.color.color_d53434))

        webview.settings.javaScriptEnabled = true
        webview.settings.setAppCacheEnabled(true)
        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showDialog()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dissDialog()
            }
        }

        webview.loadUrl(url)
    }

    companion object {
        val url = "url";
        val title = "title";
        fun startActivity(activity: Activity, url: String, title: String) {
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(this.url, url);
            intent.putExtra(this.title, title);
            activity.startActivity(intent);
        }

    }
}