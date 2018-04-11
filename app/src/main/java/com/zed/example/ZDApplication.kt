package com.zed.example

import android.support.multidex.MultiDexApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zed.common.util.GsonUtils
import com.zed.common.util.SPUtils
import com.zed.common.util.Utils
import com.zed.example.global.GlobalVariable
import com.zed.example.net.bean.Addr
import com.zed.http.HttpClient
import com.zed.http.HttpClientParam

/**
 * @author zd
 * @package
 * @fileName com.zed.example.ZDApplication
 * @date on 2017/12/12 0012 10:57
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class ZDApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        initHttpParam()
    }

    private fun initHttpParam() {

        val httpCache = SPUtils.getInstance().getString(GlobalVariable.HTTP_CACHE)
        val fromJson = arrayListOf<Addr>()
        if (httpCache != null && !httpCache.isEmpty())
            fromJson.addAll(Gson().fromJson(httpCache, object : TypeToken<List<Addr>>() {}.type))
        val param = HttpClientParam()
        param.connectTimeout = 30
        param.readTimeout = 30
        param.writeTimeout = 30
        param.retryDelaySecond = 1
        param.maxRetries = 3
        param.setUrls("http://127.0.0.1",
                "http://127.0.0.1:80",
                "http://127.0.0.1:8081")
        val https = arrayListOf<String>()
        for (url in fromJson) {
            https.add(url.url)
        }
        param.addUrls(https)
        HttpClient.setParam(param)
    }
}