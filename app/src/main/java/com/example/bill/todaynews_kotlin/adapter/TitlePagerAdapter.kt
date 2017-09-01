package com.example.bill.todaynews_kotlin.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.bill.todaynews_kotlin.base.BaseFragment

/**
 * Created by Administrator on 2016/3/30.
 */
class TitlePagerAdapter(fm: FragmentManager, private val fragments: List<BaseFragment>, private val titles: Array<String>?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): BaseFragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (titles == null) "" else titles[position]
    }

}
