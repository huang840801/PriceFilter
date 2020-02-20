package com.guanhong.pricefilter

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.customview_price_filter.view.*

class PriceFilterCustomView : LinearLayout {

    private var screenWidth = 0
    private var leftImageViewWidth = 0
    private var rightImageViewWidth = 0
    private var rootViewWidth = 0

    private var leftImageViewMarginLeft = 0
    private var rightImageViewMarginRight = 0

    private var canMoveDistance = 0

    private var rootLayoutMarginLeft = 0
    private var rootLayoutMarginRight = 0

    private var maxPrice: Int = 0
    private var minPrice: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.customview_price_filter, this)

        screenWidth = context.resources.displayMetrics.widthPixels

        leftImageView.post {

            leftImageViewWidth = leftImageView.width
        }
        rightImageView.post {

            rightImageViewWidth = rightImageView.width

            canMoveDistance = rootViewWidth - leftImageViewWidth - rightImageViewWidth
        }
        rootLayout.post {

            val coordinateArray = IntArray(2)
            rootLayout.getLocationInWindow(coordinateArray)

            rootLayoutMarginRight = (screenWidth - rootLayout.width) / 2
            rootLayoutMarginLeft = coordinateArray[0]

            rootViewWidth = rootLayout.width
        }

        Handler().postDelayed({

            Log.d("Huang", " rootViewWidth " + rootViewWidth)
            Log.d("Huang", " leftImageViewWidth " + leftImageViewWidth)
            Log.d("Huang", " rightImageViewWidth " + rightImageViewWidth)
            Log.d("Huang", " rootLayoutMarginLeft " + rootLayoutMarginLeft)
            Log.d("Huang", " rootLayoutMarginRight " + rootLayoutMarginRight)
            Log.d("Huang", " total " + getTotalWidth())

        }, 1000)
        
        leftImageView.setOnTouchListener { view, event ->
            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_MOVE -> {

                    if (event.rawX - rootLayoutMarginLeft > 0) {

                        val moveDistance = (event.rawX - rootLayoutMarginLeft)
                        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

                        if (moveDistance < rootViewWidth - rightImageViewMarginRight - leftImageViewWidth - rightImageViewWidth) {

                            layoutParams.leftMargin = moveDistance.toInt()
                        }

                        leftImageViewMarginLeft = if (layoutParams.leftMargin > 0) {

                            (layoutParams.leftMargin)
                        } else {

                            0
                        }

                        setMinPrice(minPrice + ((maxPrice - minPrice) * leftImageViewMarginLeft / canMoveDistance.toFloat()))
                    }
                }
            }

            true
        }

        rightImageView.setOnTouchListener { view, event ->

            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_MOVE -> {

                    if (event.rawX > rootLayoutMarginRight && event.rawX < rootLayoutMarginRight + rootViewWidth) {

                        val moveDistance = screenWidth - event.rawX - rootLayoutMarginRight
                        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

                        if (moveDistance < rootViewWidth - leftImageViewMarginLeft - rightImageViewWidth - leftImageViewWidth) {

                            layoutParams.rightMargin = moveDistance.toInt()
                        }

                        rightImageViewMarginRight = if (layoutParams.rightMargin > 0) {

                            layoutParams.rightMargin
                        } else {

                            0
                        }

                        setMaxPrice(maxPrice - ((maxPrice - minPrice) * rightImageViewMarginRight  / canMoveDistance.toFloat()))
                    }
                }
            }

            true
        }
    }

    private fun getTotalWidth(): Int {

        Log.d(
            "Huang",
            " MarginLeft=$leftImageViewMarginLeft, " +
                    "MarginRight=$rightImageViewMarginRight, " +
                    "canMoveDistance=$canMoveDistance, " +
                    "rrrwidth=$rightImageViewWidth, " +
                    "lllwidth=$leftImageViewWidth"
        )

        return leftImageViewMarginLeft + leftImageViewWidth + canMoveDistance + rightImageViewWidth + rightImageViewMarginRight
    }

    fun setDefaultPrice(minPrice: Int, maxPrice: Int) {

        this.maxPrice = maxPrice
        this.minPrice = minPrice

        setMaxPrice(maxPrice.toFloat())
        setMinPrice(minPrice.toFloat())
    }

    private fun setMaxPrice(maxPrice: Float) {

        highPrice.text = "$${maxPrice.toInt()}"
    }

    private fun setMinPrice(minPrice: Float) {

        lowPrice.text = "$${minPrice.toInt()}"
    }
}