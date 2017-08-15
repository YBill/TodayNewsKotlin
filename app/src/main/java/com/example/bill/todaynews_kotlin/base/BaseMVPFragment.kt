package com.example.bill.mvp_kotlin.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Bill on 2017/8/14.
 */
abstract class BaseMVPFragment<V, T : BasePresenter<V>> : Fragment() {

    protected lateinit var presenter: T

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