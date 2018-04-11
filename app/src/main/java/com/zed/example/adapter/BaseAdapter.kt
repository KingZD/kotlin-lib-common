package com.zed.example.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.zed.example.R
import com.zed.example.adapter.holder.BaseHolder
import com.zed.example.adapter.holder.HmbbHolder
import com.zed.example.listener.OnItemCLickListener
import com.zed.example.net.bean.BaseBean

/**
 * @author zd
 * @package com.zed.example.adapter
 * @fileName BaseAdapter
 * @date on 2017/12/1 0001 15:52
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
abstract class BaseAdapter<T : BaseHolder, M : BaseBean> : RecyclerView.Adapter<T> {
    protected var mContext: Context? = null
    protected var mData: List<M>? = null
    private var listener: OnItemCLickListener<M>? = null

    constructor(mContext: Context?, data: List<M>?) {
        this.mContext = mContext
        this.mData = data
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.setIndex(position)
        if (listener != null)
            holder.itemView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event?.action == MotionEvent.ACTION_DOWN)
                        listener?.onClick(mData!![position], v!!, position)
                    return false
                }
            })
//        holder.itemView.setOnClickListener { v -> listener?.onClick(mData!![position], v!!, position) }
        covert(holder, position)
    }

    abstract fun covert(holder: T, position: Int)

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    fun inflate(layoutId: Int, viewGroup: ViewGroup?, attach: Boolean): View {
        return LayoutInflater.from(mContext).inflate(layoutId, viewGroup, attach)
    }

    fun setOnItemClickListener(listener: OnItemCLickListener<M>) {
        this.listener = listener
    }
}