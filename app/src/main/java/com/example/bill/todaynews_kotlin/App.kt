package com.example.bill.todaynews_kotlin

import android.app.Application
import com.example.bill.todaynews_kotlin.delegate.DelegateExt

/**
 * Created by Bill on 2017/8/15.
 */
class App : Application() {

    companion object {
        var instance: App by DelegateExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}