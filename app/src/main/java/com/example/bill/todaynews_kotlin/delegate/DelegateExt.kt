package com.example.bill.todaynews_kotlin.delegate

import android.content.Context
import com.example.bill.todaynews_kotlin.utils.PreferenceUtils
import kotlin.properties.ReadWriteProperty

/**
 * Created by Bill on 2017/8/15.
 */
object DelegateExt {
    fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

    // 泛型函数：在函数名前声明了通用类型，从而使它可以接受任何参数
    fun <T> preference(context: Context, name: String, default: T) = PreferenceUtils(context, name, default)
}