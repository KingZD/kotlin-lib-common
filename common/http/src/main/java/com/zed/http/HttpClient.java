package com.zed.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.zed.common.util.LogUtils;
import com.zed.http.header.HeaderInterceptor;
import com.zed.http.listener.SwitchUrlListener;
import com.zed.http.log.HttpLogger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Desc  : 网络请求客户端
 */
public class HttpClient implements SwitchUrlListener {

    private volatile static HttpClient INSTANCE = null;

    private Retrofit retrofit;
    private static HttpClientParam param;

    public HttpClient() {
        //打印请求Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //okHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //打印请求log
                .addInterceptor(logInterceptor)
                //stetho, 可以在chrome中查看请求
                .addNetworkInterceptor(new StethoInterceptor())
                //添加 header Params
                .addInterceptor(new HeaderInterceptor(param))
                //失败重连
                .retryOnConnectionFailure(true)
                //time out
                .readTimeout(param.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(param.getWriteTimeout(), TimeUnit.SECONDS)
                .connectTimeout(param.getConnectTimeout(), TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder().create();

        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(okHttpClient)
                //baseUrl
                .baseUrl(param.currentUrl())
                //gson转化器
                .addConverterFactory(GsonConverterFactory.create(gson))
                //自适配
                .addCallAdapterFactory(ZedObservableFactory.create(this, param.getMaxRetries(), param.getRetryDelaySecond()))
                //Rx
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

    }

    public static HttpClient getSingleton() {
        if (INSTANCE == null) {
            synchronized (HttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClient();
                }
            }
        }
        return INSTANCE;
    }

    public <T> T getService(Class<T> t) {
        return retrofit.create(t);
    }

    @Override
    public void responseState(Throwable t) {
        LogUtils.i(t.toString());
        //这里处理超时切换baseUrl的逻辑
//        if (t instanceof TimeoutException) {
        param.nextUrlIndex();
//        }
    }

    public static void setParam(HttpClientParam param) {
        HttpClient.param = param;
    }
}
