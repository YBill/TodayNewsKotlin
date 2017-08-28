package com.example.bill.todaynews_kotlin.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Bill on 2017/8/24.
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {

    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResId(), container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onClick(p0: View?) {

    }

    abstract fun getLayoutResId(): Int

    abstract fun init()

}