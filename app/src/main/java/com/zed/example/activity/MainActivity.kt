package com.zed.example.activity

import android.view.View
import com.zed.example.R
import com.zed.example.base.BaseActivity
import com.zed.example.fragment.HomeFragment
import com.zed.example.fragment.SecondFragment
import com.zed.example.fragment.ThirdFragment
import com.zed.example.net.MyHttpService
import com.zed.example.net.bean.BaseBean
import com.zed.example.net.response.TestResponse
import com.zed.http.rx.HttpObserver
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author zd
 * @package com.zed.example.activity
 * @fileName MainActivity
 * @date on 2017/11/28 15:57
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class MainActivity : BaseActivity() {

    //"同一模块下可见"
    private var homeFragment: HomeFragment? = null
    private var secondFragment: SecondFragment? = null
    private var thirdFragment: ThirdFragment? = null
    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        selectHomeTab(R.id.rbOne)
        MyHttpService.test("ss").execOnThread(object: HttpObserver<TestResponse>(){
            override fun success(t: TestResponse?) {
            }

            override fun complete() {
            }


        },getHttpLifeRecycle())
    }

    fun showFragment(view: View) {
        selectHomeTab(view.id)
    }

    fun selectHomeTab(checkedId: Int) {
        val oCid = rgTab.tag ?: -1

        if (checkedId == R.id.rbOne && oCid != R.id.rbOne) {
            homeFragment = homeFragment ?: HomeFragment()
            repalce(homeFragment)
        } else if (checkedId == R.id.rbTwo && oCid != R.id.rbTwo) {
            secondFragment = secondFragment ?: SecondFragment()
            repalce(secondFragment)
        } else if (checkedId == R.id.rbThree && oCid != R.id.rbThree) {
            thirdFragment = thirdFragment ?: ThirdFragment()
            repalce(thirdFragment)
        } else if (checkedId == R.id.rbFour && oCid != R.id.rbFour) {
            homeFragment = homeFragment ?: HomeFragment()
            repalce(homeFragment)
        } else if (checkedId == R.id.rbFive && oCid != R.id.rbFive) {
            homeFragment = homeFragment ?: HomeFragment()
            repalce(homeFragment)
        }
        //默认设置选中的viewtag
        rgTab.tag = checkedId
        rgTab.check(checkedId)
    }
}
