package com.zed.example.net

import com.zed.example.net.bean.BaseBean
import com.zed.example.net.response.AllDataResponse
import com.zed.example.net.response.TestResponse
import com.zed.http.ZedObservable
import com.zed.http.api.BaseResponse
import retrofit2.http.*


/**
 * @author zd
 * @package com.zed.example.net.model
 * @fileName MyApi
 * @date on 2017/12/1 0001 15:59
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
interface MyApi {
    @POST("/test")
    @FormUrlEncoded
    fun test(@Field("ss") ss: String): ZedObservable<TestResponse>

    @GET("/hmbb/allData")
    fun getAllData(): ZedObservable<AllDataResponse>

}