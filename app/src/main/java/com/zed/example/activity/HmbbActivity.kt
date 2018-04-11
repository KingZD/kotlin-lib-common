package com.zed.example.activity

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.listener.OnItemClickListener
import com.zed.example.R
import com.zed.example.adapter.BaseAdapter
import com.zed.example.adapter.HmbbAdapter
import com.zed.example.adapter.holder.LocalImageHolder
import com.zed.example.base.BaseActivity
import com.zed.example.control.activity.HmbbActivityConstraint
import com.zed.example.listener.OnItemCLickListener
import com.zed.example.net.bean.Addr
import com.zed.example.net.bean.Logo
import com.zed.example.net.bean.Site
import com.zed.example.presenter.HmbbPresenter
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_hmbb.*

/**
 * Created by zed on 2018/3/30.
 */
class HmbbActivity : BaseActivity(), HmbbActivityConstraint {
    var adapter: HmbbAdapter? = null
    var mData: MutableList<Site> = arrayListOf();

    override fun setLayoutId(): Int {
        return R.layout.activity_hmbb
    }

    override fun initView() {
        appTitle.setTitle("")
        setStatusBarColor(R.color.color_d53434)
        appTitle.getTvTitle().setTextColor(getAppColor(R.color.color_ffffff))
        appTitle.setBackgroundColor(getAppColor(R.color.color_d53434))
        setPresenter(HmbbPresenter(this))
        adapter = HmbbAdapter(this, mData)
        adapter?.setOnItemClickListener(object : OnItemCLickListener<Site> {

            override fun onClick(data: Site, view: View, position: Int) {
                WebActivity.startActivity(this@HmbbActivity, data.url, data.name)
            }
        })
        xrList.layoutManager = GridLayoutManager(this, 4)
        xrList.adapter = adapter

    }

    override fun updateBanner(localImages: List<Logo>) {
        if (localImages == null) {
            convenientBanner.visibility = View.GONE
            return
        }
        convenientBanner.visibility = View.VISIBLE
        (convenientBanner as ConvenientBanner<Logo>).setPages(
                object : CBViewHolderCreator<LocalImageHolder> {
                    override fun createHolder(): LocalImageHolder {
                        return LocalImageHolder();
                    }
                }, localImages)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        convenientBanner.startTurning(2500)
        convenientBanner.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                var data = localImages.get(position);
                WebActivity.startActivity(this@HmbbActivity, data.url, data.name)
            }
        })
    }

    override fun updateTitle(title: String) {
        appTitle.setTitle(title)
    }

    override fun updateCopyRight(copyRight: String) {
        tvCopyRight.text = copyRight
    }

    override fun updateRvData(rvData: List<Site>) {
        mData.clear()
        mData.addAll(rvData)
        adapter?.notifyDataSetChanged()
    }

}