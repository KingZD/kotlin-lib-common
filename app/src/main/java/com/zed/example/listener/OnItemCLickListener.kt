package com.zed.example.listener

import android.view.View

/**
 * Created by zed on 2018/4/4.
 */
interface OnItemCLickListener<T> {
    fun onClick(data: T, view: View, position: Int)
}