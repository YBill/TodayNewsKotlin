package com.example.bill.todaynews_kotlin.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Paint.Style
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import com.example.bill.todaynews_kotlin.R


class ColorTrackView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var mTextStartX: Int = 0
    private var mTextStartY: Int = 0

    enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private var mDirection = DIRECTION_LEFT

    fun setDirection(direction: Int) {
        mDirection = direction
    }

    private var mText: String? = ""
    private val mPaint: Paint
    private var mTextSize = sp2px(30f)

    private var mTextOriginColor = 0xff000000.toInt()
    private var mTextChangeColor = 0xffff0000.toInt()

    private val mTextBound = Rect()
    private var mTextWidth: Int = 0
    private var mTextHeight: Int = 0

    private var mProgress: Float = 0.toFloat()

    init {

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.ColorTrackView)
        mText = ta.getString(R.styleable.ColorTrackView_ctvText)
        mTextSize = ta.getDimensionPixelSize(
                R.styleable.ColorTrackView_ctvText_size, mTextSize)
        mTextOriginColor = ta.getColor(
                R.styleable.ColorTrackView_ctvText_origin_color, mTextOriginColor)
        mTextChangeColor = ta.getColor(
                R.styleable.ColorTrackView_ctvText_change_color, mTextChangeColor)
        mProgress = ta.getFloat(R.styleable.ColorTrackView_ctvProgress, 0f)

        mDirection = ta
                .getInt(R.styleable.ColorTrackView_ctvDirection, mDirection)

        ta.recycle()

        mPaint.textSize = mTextSize.toFloat()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureText()

        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)

        mTextStartX = measuredWidth / 2 - mTextWidth / 2
        mTextStartY = measuredHeight / 2 - mTextHeight / 2
    }

    private fun measureHeight(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val `val` = View.MeasureSpec.getSize(measureSpec)
        var result = 0
        when (mode) {
            View.MeasureSpec.EXACTLY -> result = `val`
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                result = mTextBound.height()
                result += paddingTop + paddingBottom
            }
        }
        result = if (mode == View.MeasureSpec.AT_MOST) Math.min(result, `val`) else result
        return result
    }

    private fun measureWidth(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        var result = 0
        when (mode) {
            View.MeasureSpec.EXACTLY -> result = size
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                // result = mTextBound.width();
                result = mTextWidth
                result += paddingLeft + paddingRight
            }
        }
        result = if (mode == View.MeasureSpec.AT_MOST) Math.min(result, size) else result
        return result
    }

    private fun measureText() {
        mTextWidth = mPaint.measureText(mText).toInt()
        val fm = mPaint.fontMetrics
        mTextHeight = Math.ceil((fm.descent - fm.top).toDouble()).toInt()

        mPaint.getTextBounds(mText, 0, mText!!.length, mTextBound)
        mTextHeight = mTextBound.height()
    }

    fun reverseColor() {
        val tmp = mTextOriginColor
        mTextOriginColor = mTextChangeColor
        mTextChangeColor = tmp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val r = (mProgress * mTextWidth + mTextStartX).toInt()
        val t = (mProgress * mTextHeight + mTextStartY).toInt()

        when (mDirection) {
            DIRECTION_LEFT -> {
                drawChangeLeft(canvas, r)
                drawOriginLeft(canvas, r)
            }
            DIRECTION_RIGHT -> {
                drawOriginRight(canvas, r)
                drawChangeRight(canvas, r)
            }
            DIRECTION_TOP -> {
                drawOriginTop(canvas, t)
                drawChangeTop(canvas, t)
            }
            else -> {
                drawOriginBottom(canvas, t)
                drawChangeBottom(canvas, t)
            }
        }

    }

    private val debug = false

    private fun drawText_h(canvas: Canvas, color: Int, startX: Int, endX: Int) {
        mPaint.color = color
        if (debug) {
            mPaint.style = Style.STROKE
            canvas.drawRect(startX.toFloat(), 0f, endX.toFloat(), measuredHeight.toFloat(), mPaint)
        }
        canvas.save(Canvas.CLIP_SAVE_FLAG)
        canvas.clipRect(startX, 0, endX, measuredHeight)// left, top,
        // right, bottom
        canvas.drawText(mText!!, mTextStartX.toFloat(),
                measuredHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint)
        canvas.restore()
    }

    private fun drawText_v(canvas: Canvas, color: Int, startY: Int, endY: Int) {
        mPaint.color = color
        if (debug) {
            mPaint.style = Style.STROKE
            canvas.drawRect(0f, startY.toFloat(), measuredWidth.toFloat(), endY.toFloat(), mPaint)
        }

        canvas.save(Canvas.CLIP_SAVE_FLAG)
        canvas.clipRect(0, startY, measuredWidth, endY)// left, top,
        canvas.drawText(mText!!, mTextStartX.toFloat(),
                measuredHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint)
        canvas.restore()
    }

    private fun drawChangeLeft(canvas: Canvas, r: Int) {
        drawText_h(canvas, mTextChangeColor, mTextStartX,
                (mTextStartX + mProgress * mTextWidth).toInt())
    }

    private fun drawOriginLeft(canvas: Canvas, r: Int) {
        drawText_h(canvas, mTextOriginColor, (mTextStartX + mProgress * mTextWidth).toInt(), mTextStartX + mTextWidth)
    }

    private fun drawChangeRight(canvas: Canvas, r: Int) {
        drawText_h(canvas, mTextChangeColor,
                (mTextStartX + (1 - mProgress) * mTextWidth).toInt(), mTextStartX + mTextWidth)
    }

    private fun drawOriginRight(canvas: Canvas, r: Int) {
        drawText_h(canvas, mTextOriginColor, mTextStartX,
                (mTextStartX + (1 - mProgress) * mTextWidth).toInt())
    }

    private fun drawChangeTop(canvas: Canvas, r: Int) {
        drawText_v(canvas, mTextChangeColor, mTextStartY,
                (mTextStartY + mProgress * mTextHeight).toInt())
    }

    private fun drawOriginTop(canvas: Canvas, r: Int) {
        drawText_v(canvas, mTextOriginColor, (mTextStartY + mProgress * mTextHeight).toInt(), mTextStartY + mTextHeight)
    }

    private fun drawChangeBottom(canvas: Canvas, t: Int) {
        drawText_v(canvas, mTextChangeColor,
                (mTextStartY + (1 - mProgress) * mTextHeight).toInt(),
                mTextStartY + mTextHeight)
    }

    private fun drawOriginBottom(canvas: Canvas, t: Int) {
        drawText_v(canvas, mTextOriginColor, mTextStartY,
                (mTextStartY + (1 - mProgress) * mTextHeight).toInt())
    }

    var progress: Float
        get() = mProgress
        set(progress) {
            this.mProgress = progress
            invalidate()
        }

    var textSize: Int
        get() = mTextSize
        set(mTextSize) {
            this.mTextSize = mTextSize
            mPaint.textSize = mTextSize.toFloat()
            requestLayout()
            invalidate()
        }

    fun setText(text: String) {
        this.mText = text
        requestLayout()
        invalidate()
    }

    var textOriginColor: Int
        get() = mTextOriginColor
        set(mTextOriginColor) {
            this.mTextOriginColor = mTextOriginColor
            invalidate()
        }

    var textChangeColor: Int
        get() = mTextChangeColor
        set(mTextChangeColor) {
            this.mTextChangeColor = mTextChangeColor
            invalidate()
        }

    private fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, resources.displayMetrics).toInt()
    }

    private fun sp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, resources.displayMetrics).toInt()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putFloat(KEY_STATE_PROGRESS, mProgress)
        bundle.putParcelable(KEY_DEFAULT_STATE, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {

        if (state is Bundle) {
            mProgress = state.getFloat(KEY_STATE_PROGRESS)
            super.onRestoreInstanceState(state
                    .getParcelable(KEY_DEFAULT_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {

        private val DIRECTION_LEFT = 0
        private val DIRECTION_RIGHT = 1
        private val DIRECTION_TOP = 2
        private val DIRECTION_BOTTOM = 3

        private val KEY_STATE_PROGRESS = "key_progress"
        private val KEY_DEFAULT_STATE = "key_default_state"
    }
}
