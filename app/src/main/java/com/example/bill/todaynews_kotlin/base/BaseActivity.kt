package com.example.bill.todaynews_kotlin.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by Bill on 2017/8/14.
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        init()
    }

    abstract fun getLayoutResId(): Int

    abstract fun init()

    override fun onClick(view: View) {

    }
}