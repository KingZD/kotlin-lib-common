package com.zed.example.presenter

import com.google.gson.Gson
import com.zed.common.util.SPUtils
import com.zed.example.base.BasePresenter
import com.zed.example.control.activity.HmbbActivityConstraint
import com.zed.example.global.GlobalVariable
import com.zed.example.net.MyHttpService
import com.zed.example.net.bean.BaseBean
import com.zed.example.net.response.AllDataResponse
import com.zed.http.rx.HttpObserver

/**
 * Created by zed on 2018/4/4.
 */
class HmbbPresenter : BasePresenter<HmbbActivityConstraint, BaseBean> {


    constructor(view: HmbbActivityConstraint) : super(view)


    override fun init() {
        getAllData()
    }


    fun getAllData() {
        showDialog()
        MyHttpService.getAllData().execOnThread(object : HttpObserver<AllDataResponse>() {
            override fun success(t: AllDataResponse?) {
                dissDialog()
                getView()?.updateTitle(t?.data?.title!!.name)
                getView()?.updateCopyRight(t?.data?.copyright!!.name)
                getView()?.updateRvData(t?.data!!.siteList)
                getView()?.updateBanner(t?.data!!.logoList)
                SPUtils.getInstance().put(GlobalVariable.HTTP_CACHE, Gson().toJson(t?.data!!.addrList))
            }

            override fun complete() {
                dissDialog()
            }
        }, getView()?.getHttpLifeRecycle())
    }
}