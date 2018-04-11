package com.zed.example.adapter.holder

import android.view.View
import com.zed.example.R
import com.zed.example.base.IBaseHolderConstruction
import com.zed.example.net.bean.Site
import com.zed.image.GlideUtils
import com.zed.image.round.CornerType
import kotlinx.android.synthetic.main.item_hmbb.view.*

/**
 * Created by zed on 2018/3/30.
 */
class HmbbHolder : BaseHolder, IBaseHolderConstruction<Site> {

    constructor(itemView: View) : super(itemView)

    override fun init(bean: Site?) {
        itemView.tvName.text = bean?.name
        GlideUtils.Builder
                .with(itemView)
                .load(bean?.img)
                .placeholder(R.mipmap.tb)
                .round(10)
                .cornerType(CornerType.ALL)
                .into(itemView.ivImg)
    }
}