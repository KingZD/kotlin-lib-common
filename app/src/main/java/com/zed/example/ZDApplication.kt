package com.zed.example

import android.support.multidex.MultiDexApplication
import com.zed.common.util.Utils
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

    private fun initHttpParam(){
        val param = HttpClientParam()
        param.connectTimeout = 30
        param.readTimeout = 30
        param.writeTimeout = 30
        param.retryDelaySecond = 1
        param.maxRetries = 3
        param.setUrls("http://127.0.0.1:8080","http://192.168.0.1:8080","http://localhost:8080")
        HttpClient.setParam(param)
    }
}