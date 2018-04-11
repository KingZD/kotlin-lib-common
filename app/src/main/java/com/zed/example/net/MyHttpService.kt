package com.zed.example.net

import com.zed.example.net.bean.BaseBean
import com.zed.example.net.response.AllDataResponse
import com.zed.example.net.response.TestResponse
import com.zed.http.HttpClient
import com.zed.http.ZedObservable
import com.zed.http.api.BaseResponse

/**
 * @author zd
 * @package com.zed.example.net
 * @fileName MyHttpService
 * @date on 2017/12/4 0004 09:27
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
object MyHttpService : MyApi {
    override fun getAllData(): ZedObservable<AllDataResponse> {
        return SingletonHolder.API.getAllData()
    }

    override fun test(ss: String): ZedObservable<TestResponse> {
        return SingletonHolder.API.test(ss)
    }

    private object SingletonHolder {
        val API = HttpClient.getSingleton().getService(MyApi::class.java)!!
    }
}