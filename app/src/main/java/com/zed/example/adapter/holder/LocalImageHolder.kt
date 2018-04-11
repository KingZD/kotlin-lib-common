package com.zed.example.adapter.holder

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.Holder
import com.zed.example.R
import com.zed.example.net.bean.Logo
import com.zed.image.GlideUtils

/**
 * Created by zed on 2018/4/4.
 */
class LocalImageHolder : Holder<Logo> {

    var imageView: ImageView? = null

    override fun UpdateUI(context: Context?, position: Int, data: Logo) {
        GlideUtils
                .Builder
                .with(context)
                .error(R.mipmap.banner)
                .placeholder(R.mipmap.banner)
                .load(data.img)
                .into(imageView)
    }

    override fun createView(context: Context?): View {
        imageView = ImageView(context)
        imageView?.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView?.adjustViewBounds = true
        return imageView!!;
    }
}