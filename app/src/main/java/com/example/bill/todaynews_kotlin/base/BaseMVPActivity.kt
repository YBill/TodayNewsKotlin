package com.example.bill.mvp_kotlin.base

import android.os.Bundle
import com.example.bill.todaynews_kotlin.base.BaseActivity

/**
 * Created by Bill on 2017/8/14.
 */
abstract class BaseMVPActivity<V, T : BasePresenter<V>> : BaseActivity() {

    lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenter()
        presenter.attach(this as V)
    }

    protected abstract fun initPresenter(): T

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

}