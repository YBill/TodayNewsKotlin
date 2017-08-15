package com.example.bill.mvp_kotlin.base

/**
 * Created by Bill on 2017/8/14.
 */
abstract class BasePresenter<V> {

    protected var mView: V? = null

    fun attach(mView: V) {
        this.mView = mView
    }

    fun detach() {
        this.mView = null
    }

}