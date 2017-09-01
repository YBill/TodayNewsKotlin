package com.example.bill.todaynews_kotlin.utils

import android.content.res.Resources
import android.util.AttributeSet


/**
 * Created by Bill on 2017/8/30.
 */
object ViewAttributeUtil {

    fun getAttributeValue(attr: AttributeSet, paramInt: Int): Int {
        var value = -1
        val count = attr.attributeCount
        for (i in 0 until count) {
            if (attr.getAttributeNameResource(i) === paramInt) {
                val str = attr.getAttributeValue(i)
                if (null != str && (str!!.startsWith("?") || paramInt == android.R.attr.src || paramInt == android.R.attr.background)) {
                    //因为要动态改图片，图片没有用自定义属性
                    value = Integer.valueOf(str!!.substring(1, str!!.length))!!.toInt()
                    return value
                }
            }
        }
        return value
    }

    fun getBackgroundAttibute(attr: AttributeSet): Int {
        return getAttributeValue(attr, android.R.attr.background)
    }
}