package com.zed.example.base

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.trello.rxlifecycle2.components.support.RxFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaeger.library.StatusBarUtil
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.zed.common.util.UiUtil
import com.zed.example.control.activity.UIActivityConstraint
import com.zed.example.dialog.LoadingDialog
import com.zed.example.net.bean.BaseBean
import java.util.*

/**
 * @author zd
 * @package com.zed.example.base
 * @fileName BaseFragment
 * @date on 2017/12/1 0001 11:46
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
abstract class BaseFragment : BasePresenterFragment<BasePresenter<*, *>>(), UIActivityConstraint {
    internal var dialog: LoadingDialog? = null

    override fun showDialog() {
        if (activity == null) return
        if (dialog == null) {
            dialog = LoadingDialog(context!!)
        }
        dialog?.show()
    }

    override fun dissDialog() {
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }
    }
}