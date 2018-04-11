package com.zed.example.base

import com.zed.example.control.activity.UIActivityConstraint
import com.zed.example.dialog.LoadingDialog
import com.zed.example.net.bean.BaseBean

/**
 * @author zd
 * @package com.zed.example.base
 * @fileName BaseActivity
 * @date on 2017/11/28 15:57
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 *
 * 拥有BasePresenterActivity所有属性 并且不需要强制泛型
 */

abstract class BaseActivity : BasePresenterActivity<BasePresenter<*, *>>(), UIActivityConstraint {

    internal var dialog: LoadingDialog? = null

    override fun showDialog() {
        if (isFinishing) return
        if (dialog == null) {
            dialog = LoadingDialog(this)
        }
        dialog?.show()
    }

    override fun dissDialog() {
        if (isFinishing) return
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }
    }
}