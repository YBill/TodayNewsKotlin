package com.example.bill.todaynews_kotlin.fragment

import android.os.Bundle
import android.view.View
import com.example.bill.todaynews_kotlin.R
import com.example.bill.todaynews_kotlin.adapter.TitlePagerAdapter
import com.example.bill.todaynews_kotlin.base.BaseFragment
import com.example.bill.todaynews_kotlin.view.ColorTrackTabViewIndicator
import com.example.bill.todaynews_kotlin.view.ColorTrackView
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * Created by Bill on 2017/8/28.
 */
class HomeFragment : BaseFragment() {

    private val titles = arrayOf("推荐", "视频", "热点", "社会", "娱乐", "科技", "汽车", "体育", "财经", "军事", "国际", "时尚", "游戏", "旅游", "历史", "探索", "美食", "育儿", "养生", "故事", "美文")
    private val titlesCode = arrayOf("__all__", "video", "news_hot", "news_society", "news_entertainment", "news_tech", "news_car", "news_sports", "news_finance", "news_military", "news_world", "news_fashion", "news_game", "news_travel", "news_history", "news_discovery", "news_food", "news_baby", "news_regimen", "news_story", "news_essay")

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        initHeader()
    }

    private fun initHeader() {
        val fragments = ArrayList<BaseFragment>()
        for (i in 0 until titles.size) {
            val fragment = NewsListFragment()
            val bundle = Bundle()
            bundle.putString("data", titlesCode[i])
            fragment.arguments = bundle
            fragments.add(fragment)
        }
        vp.adapter = TitlePagerAdapter(childFragmentManager, fragments, titles)
        tab.setTitles(titles, object : ColorTrackTabViewIndicator.CorlorTrackTabBack {
            override fun onClickButton(position: Int?, colorTrackView: ColorTrackView) {
                position?.let { vp.setCurrentItem(it, false) }
            }
        })
        val tabChild = tab.getChildAt(0)
        val w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED)
        //重新测量
        tabChild.measure(w, h)
        //设置最小宽度，使其可以在滑动一部分距离
        tabChild.minimumWidth = tabChild.measuredWidth + tab.tabWidth

        vp.offscreenPageLimit = titles.size
        tab.setupViewPager(vp)
    }

}