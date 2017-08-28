package com.example.bill.todaynews_kotlin.ui.activity

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bill.todaynews_kotlin.R
import com.example.bill.todaynews_kotlin.base.BaseActivity
import com.example.bill.todaynews_kotlin.control.HomeFragmentControl
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    private lateinit var mController: HomeFragmentControl
    private var lastSelectedIcon: View? = null
    private var lastSelectedText: View? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
    }

    override fun init() {
        initView()
        addListener()

        mController = HomeFragmentControl.getInstance(this, R.id.fl_content)
        mController.showFragment(0)
    }

    private fun initView() {
        setSelectIcon(iv_tab_home, tv_tab_home)
    }

    private fun addListener() {
        for (i in 0 until ll_bottom.childCount) {
            ll_bottom.getChildAt(i).setOnClickListener({ v ->
                lastSelectedIcon?.isSelected = false
                lastSelectedText?.isSelected = false

                val rl = v as RelativeLayout
                val icon = rl.getChildAt(0) as ImageView
                val text = rl.getChildAt(1) as TextView
                mController.showFragment(i)
                setSelectIcon(icon, text)
            })
        }
    }

    private fun setSelectIcon(iv: ImageView, tv: TextView) {
        iv.isSelected = true
        tv.isSelected = true
        lastSelectedIcon = iv
        lastSelectedText = tv
    }

    override fun onDestroy() {
        super.onDestroy()
        HomeFragmentControl.onDestroy()
    }
}
