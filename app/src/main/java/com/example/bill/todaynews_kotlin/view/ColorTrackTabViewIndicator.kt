package com.example.bill.todaynews_kotlin.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.example.bill.todaynews_kotlin.R
import com.example.bill.todaynews_kotlin.utils.ViewAttributeUtil
import java.util.*


class ColorTrackTabViewIndicator @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null,
                                                           defStyle: Int = 0) : HorizontalScrollView(mContext, attrs, defStyle) {
    private val mTabTextSize: Int
    private val mTabSelectedTextColor: Int
    private val mPaint = Paint()
    private val mIndicatorColor: Int
    private var mTitles: Array<String>? = null
    private var mTabCount: Int = 0
    private val mTabTextColor: Int
    private var icallBack: CorlorTrackTabBack? = null
    private val viewList = ArrayList<ColorTrackView>()
    private var mTranslationX: Float = 0.toFloat()
    var tabWidth: Int = 0
    private var mAttr_textColor = -1
    private var mAttr_selectedTextColor = -1
    private var mAttr_background = -1
    private val mMode: Int
    private val mTabStrip: LinearLayout

    init {
        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.ColorTrackTabViewIndicator)

        mIndicatorColor = ta.getColor(R.styleable.ColorTrackTabViewIndicator_ctTabIndicatorColor, -1)
        mTabTextColor = ta.getColor(R.styleable.ColorTrackTabViewIndicator_ctTabTextColor, Color.YELLOW)
        mAttr_textColor = ViewAttributeUtil.getAttributeValue(attrs!!, R.attr.ctTabTextColor)
        mTabSelectedTextColor = ta.getColor(R.styleable.ColorTrackTabViewIndicator_ctTabSelectedTextColor, Color.YELLOW)
        mAttr_selectedTextColor = ViewAttributeUtil.getAttributeValue(attrs!!, R.attr.ctTabSelectedTextColor)
        mTabTextSize = ta.getDimension(R.styleable.ColorTrackTabViewIndicator_ctTabTextSize, sp2px(context, 14f).toFloat()).toInt()

        //自定义属性的background
        mAttr_background = ViewAttributeUtil.getBackgroundAttibute(attrs!!)

        tabWidth = ta.getDimension(R.styleable.ColorTrackTabViewIndicator_ctTabWidth, -1f).toInt()
        mMode = ta.getInt(R.styleable.ColorTrackTabViewIndicator_ctMode, MODE_FIXED)
        ta.recycle()
        if (mIndicatorColor != -1) {
            this.mPaint.color = mIndicatorColor
            this.mPaint.strokeWidth = 2.0f
        }

        mTabStrip = LinearLayout(mContext)
        mTabStrip.orientation = LinearLayout.HORIZONTAL
        mTabStrip.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(mTabStrip)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (this.mTabCount != 0 && tabWidth == -1) {
            this.tabWidth = w / this.mTabCount
        }
    }

    fun setupViewPager(vp: ViewPager?) {
        if (vp == null) return

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var prePosition = -1

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                tabScrolled(position, positionOffset)
                var x = calculateScrollXForTab(position, positionOffset)
                smoothScrollTo(x, 0)
            }

            override fun onPageSelected(position: Int) {
                if (prePosition != -1) {
                    val colorTrackView = viewList[prePosition]
                    colorTrackView.progress = 0f
                }
                prePosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun calculateScrollXForTab(position: Int, positionOffset: Float): Int {
        if (mMode == MODE_SCROLLABLE) {
            val selectedChild = mTabStrip.getChildAt(position)
            val nextChild = if (position + 1 < mTabStrip.childCount)
                mTabStrip.getChildAt(position + 1)
            else
                null
            val selectedWidth = selectedChild?.width ?: 0
            val nextWidth = nextChild?.width ?: 0

            return selectedChild!!.left
            +((selectedWidth + nextWidth).toFloat() * positionOffset * 0.5f).toInt()
            +(selectedChild!!.width / 2) - width / 2
        }
        return 0
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mIndicatorColor != -1) {
            canvas.save()
            canvas.translate(mTranslationX, (height - 2).toFloat())
            canvas.drawLine(0.0f, 0.0f, tabWidth.toFloat(), 0.0f, this.mPaint)
            canvas.restore()
        }
    }

    fun tabScrolled(position: Int, positionOffset: Float) {
        this.mTranslationX = width / this.mTabCount * (position + positionOffset)

        if (positionOffset == 0.0f) {
            return
        }
        val currentTrackView = this.viewList[position]
        val nextTrackView = this.viewList[position + 1]
        currentTrackView.setDirection(1)
        currentTrackView.progress = 1.0f - positionOffset
        preColorTrackView = currentTrackView
        nextTrackView.setDirection(0)
        nextTrackView.progress = positionOffset
        invalidate()
    }

    fun setTitles(titles: Array<String>, callBack: CorlorTrackTabBack) {
        this.mTitles = titles
        this.mTabCount = titles.size
        this.icallBack = callBack
        generateTitleView()
    }

    private var preColorTrackView: ColorTrackView? = null

    private fun generateTitleView() {
        if (mTabStrip.childCount > 0) {
            mTabStrip.removeAllViews()
        }
        for (i in mTitles!!.indices) {
            val tabLayout = LinearLayout(context)
            tabLayout.orientation = LinearLayout.HORIZONTAL
            tabLayout.gravity = Gravity.CENTER
            tabLayout.tag = Integer.valueOf(i)
            tabLayout.setOnClickListener { v ->
                if (preColorTrackView != null) {
                    preColorTrackView!!.progress = 0f
                }
                val index = v.tag as Int
                val colorTrackView = (mTabStrip.getChildAt(index) as LinearLayout)
                        .getChildAt(0) as ColorTrackView
                colorTrackView.progress = 1f
                if (icallBack != null) {
                    icallBack!!.onClickButton(index, colorTrackView)
                }
                preColorTrackView = colorTrackView
                mTranslationX = (width / mTabCount * index).toFloat()
                invalidate()
            }

            val colorTrackView = ColorTrackView(context)
            var params: FrameLayout.LayoutParams? = null
            if (mMode == MODE_FIXED) {
                val widthPixels = mContext.resources.displayMetrics.widthPixels
                params = FrameLayout.LayoutParams(if (tabWidth == -1) widthPixels / mTitles!!.size else tabWidth, -2)
            } else {
                params = FrameLayout.LayoutParams(if (tabWidth == -1) -2 else tabWidth, -2)
            }

            colorTrackView.layoutParams = params
            colorTrackView.tag = Integer.valueOf(i)
            colorTrackView.setText(this.mTitles!![i])
            colorTrackView.textOriginColor = mTabTextColor
            colorTrackView.textChangeColor = mTabSelectedTextColor
            colorTrackView.textSize = mTabTextSize
            if (i == 0) {
                colorTrackView.progress = 1.0f
                preColorTrackView = colorTrackView
            }
            tabLayout.addView(colorTrackView)
            viewList.add(colorTrackView)
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
            //            layoutParams.weight = 1.0F;
            tabLayout.layoutParams = layoutParams
            mTabStrip.addView(tabLayout)
        }
    }

    val view: View
        get() = this

    interface CorlorTrackTabBack {
        fun onClickButton(position: Int?, colorTrackView: ColorTrackView)
    }

    companion object {

        val MODE_SCROLLABLE = 0

        val MODE_FIXED = 1

        /**
         * 将sp值转换为px值，保证文字大小不变
         *
         * @param spValue （DisplayMetrics类中属性scaledDensity）
         * @return
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}
