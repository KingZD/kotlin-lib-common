package com.zed.example.control.activity

import android.app.Activity
import com.trello.rxlifecycle2.LifecycleTransformer
import com.zed.example.base.IBaseConstruction
import com.zed.example.net.bean.Logo
import com.zed.example.net.bean.Site

/**
 * Created by zed on 2018/4/4.
 */
interface HmbbActivityConstraint : IBaseConstruction {
    fun updateTitle(title: String)
    fun updateCopyRight(copyRight: String)
    fun updateRvData(copyRight: List<Site>)
    fun updateBanner(localImages: List<Logo>)
}