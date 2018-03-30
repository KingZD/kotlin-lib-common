package com.zed.http.listener;

/**
 * Created by zed on 2018/3/28.
 * 监听网络状态后 切换baseUrl
 */

public interface SwitchUrlListener {
    void responseState(Throwable t);
}
