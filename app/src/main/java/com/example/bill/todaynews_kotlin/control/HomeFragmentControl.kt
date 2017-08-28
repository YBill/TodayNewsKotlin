package com.example.bill.todaynews_kotlin.control


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.example.bill.todaynews_kotlin.fragment.HomeFragment
import java.util.*


/**
 * 主界面Fragment控制器
 */
class HomeFragmentControl private constructor(activity: FragmentActivity, private val containerId: Int) {

    private val fm: FragmentManager = activity.supportFragmentManager
    private lateinit var fragments: ArrayList<Fragment>

    init {
        initFragment()
    }

    private fun initFragment() {
        fragments = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(HomeFragment())
        fragments.add(HomeFragment())
        fragments.add(HomeFragment())

        val ft = fm.beginTransaction()
        for (i in fragments.indices) {
            ft.add(containerId, fragments[i], "" + i)
        }
        ft.commit()
    }

    fun showFragment(position: Int) {
        hideFragments()
        val fragment = fragments[position]
        val ft = fm.beginTransaction()
        ft.show(fragment)
        ft.commitAllowingStateLoss()
    }

    private fun hideFragments() {
        val ft = fm.beginTransaction()
        fragments.forEach { ft.hide(it) }
        ft.commitAllowingStateLoss()
    }

    companion object {

        private var controller: HomeFragmentControl? = null

        fun getInstance(activity: FragmentActivity, containerId: Int): HomeFragmentControl {
            if (controller == null) {
                controller = HomeFragmentControl(activity, containerId)
            }
            return controller as HomeFragmentControl
        }

        fun onDestroy() {
            controller = null
        }
    }
}