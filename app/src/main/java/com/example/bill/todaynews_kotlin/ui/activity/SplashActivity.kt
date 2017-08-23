package com.example.bill.todaynews_kotlin.ui.activity

import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import com.example.bill.mvp_kotlin.base.BaseMVPActivity
import com.example.bill.todaynews_kotlin.MainActivity
import com.example.bill.todaynews_kotlin.R
import com.example.bill.todaynews_kotlin.ui.contract.BaseSplashPresenter
import com.example.bill.todaynews_kotlin.ui.contract.SplashView
import com.example.bill.todaynews_kotlin.ui.presenter.SplashPresenter
import com.example.bill.todaynews_kotlin.utils.ImageLoadUtils
import com.example.bill.todaynews_kotlin.utils.launchActivity
import com.example.bill.todaynews_kotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.find

class SplashActivity : BaseMVPActivity<SplashView, BaseSplashPresenter>(), SplashView {

    var timer: CountDownTimer? = null
    lateinit var bgImage: ImageView

    override fun initPresenter(): BaseSplashPresenter {
        return SplashPresenter()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        initView()
        loadData()
        addListener()
    }

    private fun initView() {
        bgImage = find(R.id.iv_bg)
    }

    private fun loadData() {
        ImageLoadUtils.splashDisplay(this, bgImage, "http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg",
                object : ImageLoadUtils.ImageLoadListener {
                    override fun onSuccess() {
                        tv_click_look.visibility = View.VISIBLE
                    }

                    override fun onFail() {
                        tv_click_look.visibility = View.GONE
                    }
                })

        timer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                startActivity()
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_ad_time.text = "${millisUntilFinished / 1000}s跳过广告"
            }

        }.start()
    }

    private fun addListener() {
        tv_ad_time.setOnClickListener(this)
        tv_click_look.setOnClickListener(this)
    }

    private fun startActivity() {
        launchActivity<HomeActivity>()
        finish()
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.tv_ad_time -> {
                startActivity()
            }
            R.id.tv_click_look -> showToast("广告")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()

    }

}
