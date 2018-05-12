package com.zed.http.header;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zed.common.util.LogUtils;
import com.zed.http.HttpClientParam;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

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
        RequestBody requestBody = request.body();
        HttpUrl requestUrl = request.url();
        //創建新的reqBuilder并赋予参数
        final Request.Builder newBuilder = request.newBuilder();
        newBuilder.url(requestUrl);
        newBuilder.headers(request.headers());
        newBuilder.tag(request.tag());
        newBuilder.method(request.method(), requestBody);
        //创建新的http相关的数据体
        HttpUrl.Builder builder = new HttpUrl.Builder();
        HttpUrl parse;
        String requestParam = "";
        String originalRequestUrl = requestUrl.toString();
        String currentUrl = mParam.currentUrl();
        HttpUrl currParse = HttpUrl.parse(currentUrl);
        //这里是为了过滤含有path的url 因为只需要地址就可以
        //ex: http://xxx:80/aa/bb => http://xxx:80
        currentUrl = currParse.scheme() + "://" + currParse.host() + ":" + currParse.port();
        //比较上次失败的api地址和本次需要轮询的地址是否 一致
        if (!originalRequestUrl.startsWith(currentUrl)) {
            //不一致的则使用新的地址路劲
            parse = HttpUrl.parse(currentUrl);
        } else {
            //否则使用原本的
            parse = requestUrl;
        }
        //添加路劲path构成完整的url
        List<String> pathSegments = requestUrl.pathSegments();
        if (pathSegments != null)
            for (String path : pathSegments) {
                builder.addPathSegment(path);
            }
        builder.username(requestUrl.username());
        builder.password(requestUrl.password());
        //根据http method 来进行body或者Query参数的添加
        if (request.method().equals("GET")) {
            //(GET请求)query参数赋值
            Set<String> strings = requestUrl.queryParameterNames();
            if (strings != null)
                for (String pm : strings) {
                    builder.addQueryParameter(pm, requestUrl.queryParameter(pm));
                }
            requestParam = requestUrl.query();
            newBuilder.method(request.method(), requestBody);
        } else {
            if (request.body() != null && request.body() instanceof FormBody) {
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                if (isPlaintext(buffer)) {
                    String param = buffer.readString(charset);
                    requestParam = param;
                    LogUtils.i(param);
                    String[] split = param.split("&");
                    FormBody.Builder fb = new FormBody.Builder();
                    for (String str : split) {
                        String[] kv = str.split("=");
                        if (kv.length > 1) {
                            fb.add(kv[0], kv[1]);
//                            builder.addQueryParameter(kv[0], kv[1]);
                        }
                    }
                    newBuilder.method(request.method(), fb.build());
                }
            } else if (request.body() != null && request.body().contentType().subtype().equalsIgnoreCase("json")) {
                newBuilder.method(request.method(), requestBody);
            } else {
                newBuilder.method(request.method(), requestBody);
            }
        }
        builder.port(parse.port());
        builder.host(parse.host());
        builder.scheme(parse.scheme());
        newBuilder.url(builder.build());

        Map<String, String> headerParams = getHeader(request);
        for (Map.Entry<String, String> ent : headerParams.entrySet()) {
            if (!TextUtils.isEmpty(ent.getValue())) {
                newBuilder.addHeader(ent.getKey(), ent.getValue());
            } else {
                newBuilder.addHeader(ent.getKey(), "");
            }
        }
        Request build = newBuilder.build();
        LogUtils.d("param: " + requestParam);
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

    boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
