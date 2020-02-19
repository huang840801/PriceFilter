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

    private var leftDistance = 0
    private var rightDistance = 0

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

        rootLayout.post {

            val coordinateArray = IntArray(2)
            rootLayout.getLocationInWindow(coordinateArray)

            rootLayoutMarginRight = (screenWidth - rootLayout.width) / 2
            rootLayoutMarginLeft = coordinateArray[0]

            rootViewWidth = rootLayout.width
        }

        leftImageView.post {

            leftImageViewWidth = leftImageView.width
        }
        rightImageView.post {

            rightImageViewWidth = rightImageView.width
        }

        Handler().postDelayed({

            Log.d("Huang", " leftImageViewWidth " + leftImageViewWidth)
            Log.d("Huang", " rightImageViewWidth " + rightImageViewWidth)
            Log.d("Huang", " rootViewWidth " + rootViewWidth)
            Log.d("Huang", " rootLayoutMarginLeft " + rootLayoutMarginLeft)
            Log.d("Huang", " rootLayoutMarginRight " + rootLayoutMarginRight)

        }, 1000)
        leftImageView.setOnTouchListener { view, event ->
            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_MOVE -> {

//                    Log.d("Huang", " event.rawX ${event.rawX}")
//                    Log.d("Huang", " event.X ${event.x}")


                    val moveDistance =
                        (event.rawX - rootLayoutMarginLeft - leftImageViewWidth / 2).toInt()

                    Log.d("Huang", " moveDistance ${moveDistance}")

                    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

                    if (moveDistance < rootViewWidth - rightDistance - leftImageViewWidth / 2 - rightImageViewWidth) {

                        layoutParams.leftMargin = moveDistance
                    }

                    leftDistance = if (layoutParams.leftMargin > 0) {

                        (layoutParams.leftMargin)
                    } else {

                        0
                    }


                    setMinPrice(minPrice + (leftDistance * (maxPrice - minPrice) / rootViewWidth) + rootLayoutMarginLeft)
                }
            }

            true
        }

        rightImageView.setOnTouchListener { view, event ->

            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_MOVE -> {

                    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

                    val moveDistance =
                        (screenWidth - event.rawX - rootLayoutMarginRight - rightImageViewWidth / 2).toInt()

//                    Log.d("Huang", "  layoutParams.rightMargin " +  layoutParams.rightMargin)
//                    Log.d("Huang", "  leftDistance " +  leftDistance)
                    Log.d("Huang", " not move ")

                    if (moveDistance < rootViewWidth - leftDistance - rightImageViewWidth / 2 - leftImageViewWidth) {

                        Log.d("Huang", " move ")

                        layoutParams.rightMargin =
                            (screenWidth - event.rawX - rootLayoutMarginRight - rightImageViewWidth / 2).toInt()
//                        view.layoutParams = layoutParams
                    }

                    rightDistance = if (layoutParams.rightMargin > 0) {

                        layoutParams.rightMargin
                    } else {

                        0
                    }

//                    Log.d("Huang", " right " + rightDistance)

                    setMaxPrice(maxPrice - (rightDistance * (maxPrice - minPrice) / rootViewWidth))

                }
            }

            true
        }
    }

    fun setDefaultPrice(minPrice: Int, maxPrice: Int) {

        this.maxPrice = maxPrice
        this.minPrice = minPrice

        setMaxPrice(maxPrice)
        setMinPrice(minPrice)
    }

    private fun setMaxPrice(maxPrice: Int) {

        highPrice.text = "$$maxPrice"
    }

    private fun setMinPrice(minPrice: Int) {

        lowPrice.text = "$$minPrice"
    }
}