package com.zed.example.adapter

import android.content.Context
import android.view.ViewGroup
import com.zed.example.R
import com.zed.example.adapter.holder.BaseHolder
import com.zed.example.adapter.holder.HmbbHolder
import com.zed.example.net.bean.Logo
import com.zed.example.net.bean.Site

/**
 * Created by zed on 2018/3/30.
 */
class HmbbAdapter : BaseAdapter<HmbbHolder, Site> {

    constructor(mContext: Context?, data: List<Site>?) : super(mContext, data)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HmbbHolder {
        return HmbbHolder(inflate(R.layout.item_hmbb, parent, false))
    }

    override fun covert(holder: HmbbHolder, position: Int) {
        holder.init(mData?.get(position))
    }

}