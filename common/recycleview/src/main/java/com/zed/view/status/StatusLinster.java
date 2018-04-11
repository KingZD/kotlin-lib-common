package com.zed.view.status;

/**
 * Created by zed on 2018/4/9.
 */

public interface StatusLinster {
    void changeStatus(StatusView.Status status);

    void changeStatus(StatusView.Status status, int imgId);

    void changeStatus(StatusView.Status status, int imgId, int stringId);

    void changeStatus(StatusView.Status status, int imgId, String txt);
}
