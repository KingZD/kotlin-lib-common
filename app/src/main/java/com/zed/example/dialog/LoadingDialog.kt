package com.zed.example.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.zed.example.R
import com.zed.example.base.BaseDialog
import com.zed.image.GlideUtils
import kotlinx.android.synthetic.main.view_loading.*

/**
 * Created by zed on 2018/4/9.
 */
class LoadingDialog : BaseDialog {


    constructor(context: Context) : super(context) {
        initViews()
    }

    constructor(context: Context, fromButton: Boolean) : super(context, fromButton) {
        initViews()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        initViews()
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        initViews()
    }

    fun showDialog(context: Context, message: String): LoadingDialog {
        val dialog = LoadingDialog(context, R.style.dialog)
        dialog.setContent(message)
        dialog.show()
        return dialog
    }

    internal fun initViews() {
        setContentView(R.layout.view_loading)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        (spin_kit.background as AnimationDrawable).start()
//        GlideUtils.Builder.with(context).asGif().load(R.drawable.loading).into(spin_kit)
    }

    override fun dismiss() {
        super.dismiss()
        (spin_kit.background as AnimationDrawable).stop()
    }

    fun setContent(content: String) {
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE)
        } else {
            tvContent.setVisibility(View.VISIBLE)
            tvContent.setText(content)
        }
    }

    fun dismiss(delayMills: Long) {
        Handler().postDelayed({ dismiss() }, delayMills)
    }
}