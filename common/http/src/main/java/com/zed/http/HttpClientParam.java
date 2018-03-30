package com.zed.http;

/**
 * Created by zed on 2018/3/28.
 * 参数配置
 */

public class HttpClientParam {
    private long readTimeout;
    private long writeTimeout;
    private long connectTimeout;
    //尝试重连的间隔时间
    private int retryDelaySecond;
    //多个服务器地址
    private String[] urls;
    private int currentUrlIndex;
    //重试多少次 ps如果有多个服务器则默认重试的是切换地址请求
    //如果只有一个 服务器地址则为请求相应次数当前地址
    //如果maxRetries = 0 则不会重试
    private int maxRetries;

    /**
     * 如果请求超时的情况下 获取下一个url进行请求
     *
     * @return
     */
    public String currentUrl() {
        if (urls == null) return null;
        if (urls.length <= currentUrlIndex) return urls[0];
        return urls[currentUrlIndex];
    }

    /**
     * 如果请求超时的情况下 获取下一个url进行请求
     *
     * @return
     */
    public void nextUrlIndex() {
        currentUrlIndex++;
        if (currentUrlIndex >= urls.length)
            currentUrlIndex = 0;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String... urls) {
        this.urls = urls;
    }

    public int getRetryDelaySecond() {
        return retryDelaySecond;
    }

    public void setRetryDelaySecond(int retryDelaySecond) {
        this.retryDelaySecond = retryDelaySecond;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
