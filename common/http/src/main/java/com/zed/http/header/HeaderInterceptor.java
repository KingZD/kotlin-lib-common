package com.zed.http.header;

import android.text.TextUtils;

import com.zed.common.util.LogUtils;
import com.zed.http.HttpClientParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Desc  : header 参数拦截器
 */
public class HeaderInterceptor implements Interceptor {
    HttpClientParam mParam;

    public HeaderInterceptor(HttpClientParam param) {
        this.mParam = param;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        final Request.Builder newBuilder = request.newBuilder();
        HttpUrl requestUrl = request.url();
        newBuilder.url(requestUrl);
        newBuilder.method(request.method(), request.body());
        newBuilder.headers(request.headers());
        newBuilder.tag(request.tag());

        HttpUrl.Builder builder = new HttpUrl.Builder();
        HttpUrl parse;
        String originalRequestUrl = requestUrl.toString();
        String currentUrl = mParam.currentUrl();
        HttpUrl currParse = HttpUrl.parse(currentUrl);
        //这里是为了过滤含有path的url 因为只需要地址就可以
        //ex: http://xxx:80/aa/bb => http://xxx:80
        currentUrl = currParse.scheme() + "://" + currParse.host();
        if (!originalRequestUrl.startsWith(currentUrl)) {
            parse = HttpUrl.parse(currentUrl);
        } else {
            parse = requestUrl;
        }
        List<String> pathSegments = requestUrl.pathSegments();
        if (pathSegments != null)
            for (String path : pathSegments) {
                builder.addPathSegment(path);
            }
        builder.username(requestUrl.username());
        builder.password(requestUrl.password());
        Set<String> strings = parse.queryParameterNames();
        if (strings != null)
            for (String pm : strings) {
                builder.addQueryParameter(pm, parse.queryParameter(pm));
            }
        builder.port(parse.port());
        builder.host(parse.host());
        builder.scheme(parse.scheme());
        newBuilder.url(builder.build());

        // add header
        Map<String, String> headerParams = getHeader(request);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> ent : headerParams.entrySet()) {
            if (!TextUtils.isEmpty(ent.getValue())) {
                newBuilder.addHeader(ent.getKey(), ent.getValue());
                sb.append(ent.getKey()).append("=").append(ent.getValue()).append(", ");
            } else {
                newBuilder.addHeader(ent.getKey(), "");
                sb.append(ent.getKey()).append("=").append(" ").append(", ");
            }
        }
        Request build = newBuilder.build();
        LogUtils.d("Header: " + sb.toString());
        LogUtils.i("originalRequest url is " + originalRequestUrl
                + "\ncurrent param url is " + currentUrl
                + "\nswitch url is " + build.url());
        return chain.proceed(build);
    }

    /*
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request.Builder newBuilder = originalRequest.newBuilder();
            String originalRequestUrl = originalRequest.url().toString();
            if (!originalRequestUrl.startsWith(mParam.currentUrl())) {
                StringBuffer currUrl = new StringBuffer(mParam.currentUrl());
                List<String> pathSegments = originalRequest.url().pathSegments();
                if(pathSegments != null){
                    for (String path : pathSegments) {
                        currUrl.append("/");
                        currUrl.append(path);
                    }
                }
                newBuilder.url(currUrl.toString());
            }
            originalRequest.newBuilder().url("sssssssss");
            // add header
            Map<String, String> headerParams = getHeader(originalRequest);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> ent : headerParams.entrySet()) {
                if (!TextUtils.isEmpty(ent.getValue())) {
                    newBuilder.addHeader(ent.getKey(), ent.getValue());
                    sb.append(ent.getKey()).append("=").append(ent.getValue()).append(", ");
                } else {
                    newBuilder.addHeader(ent.getKey(), "");
                    sb.append(ent.getKey()).append("=").append(" ").append(", ");
                }
            }
            LogUtils.d("Header: " + sb.toString());
            Request newRequest = newBuilder.build();
            LogUtils.i("originalRequest url is " + originalRequestUrl
                    + "\ncurrent param url is " + mParam.currentUrl()
                    + "\nswitch url is " + newRequest.url().toString());
            return chain.proceed(newRequest);
        }
    */
    private Map<String, String> getHeader(Request originalRequest) {
        Map<String, String> headerParams = new HashMap<>();

        // sign params
        headerParams.put("device-id", HeaderParams.getDevice_id());
        headerParams.put("app-version", HeaderParams.getApp_version());
        headerParams.put("sys-version", HeaderParams.getSys_version());
        headerParams.put("uuid", HeaderParams.getUUID());
        headerParams.put("appkey", HeaderParams.getAppkey());
        headerParams.put("token", HeaderParams.getToken());
        headerParams.put("timestamp", HeaderParams.getTimestamp());
        headerParams.put("model", HeaderParams.getModel());
        headerParams.put("platform", HeaderParams.getPlatform());

        Map<String, String> signParams = new HashMap<>();
        signParams.putAll(headerParams);
        headerParams.put("sign", HeaderParams.getSign(originalRequest, signParams));

        // other params
        headerParams.put("User-Agent", HeaderParams.getUser_agent());
        headerParams.put("Accept", HeaderParams.getAccept());
        headerParams.put("Channel", HeaderParams.getChannel());
        headerParams.put("app-type", HeaderParams.getApp_type());
        headerParams.put("PushId", HeaderParams.getPush_id());
        headerParams.put("Umeng", HeaderParams.getUmeng_id());
        headerParams.put("Content-Type", HeaderParams.getContent_type());
        return headerParams;
    }
}
