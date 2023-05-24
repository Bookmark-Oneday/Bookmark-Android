package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.PartialWriteTodayOnelinetVerticalSeekbarBinding
import kotlin.math.roundToInt

class OnelineVerticalSeekbar(context : Context, attrs : AttributeSet) : FrameLayout(context, attrs) {
    private val binding : PartialWriteTodayOnelinetVerticalSeekbarBinding
    private var progressValueChangeCallback : (Int) -> Unit = {}
    private var layoutHeight = NOT_INIT
    private var yCursorDown = NOT_INIT_FLOAT
    private var thumbSize = NOT_INIT
    private val maxYPosition : Int get() { return layoutHeight - thumbSize }

    private val min : Int
    private val max : Int

    init {
        val inflater = LayoutInflater.from(context)
        binding = PartialWriteTodayOnelinetVerticalSeekbarBinding.inflate(inflater, this, true)

        initDragEvent()

        context.theme.obtainStyledAttributes(attrs, R.styleable.OnelineVerticalSeekbar, 0, 0).run {
            try {
                min = getInteger(R.styleable.OnelineVerticalSeekbar_minValue, 11)
                max = getInteger(R.styleable.OnelineVerticalSeekbar_maxValue, 40)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        layoutHeight = binding.root.measuredHeight
        thumbSize = binding.flWriteTodayOnelineSeekbarThumb.measuredHeight
    }

    // 터치 이벤트가 없어서, 굳이 performClick 을 호출하지 않음
    @SuppressLint("ClickableViewAccessibility")
    private fun initDragEvent() {
        binding.flWriteTodayOnelineSeekbarThumb.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    yCursorDown = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newY = event.rawY + yCursorDown
                    if (checkInHeight(newY)) {
                        val value = positionToProgressValue(newY)
                        progressValueChangeCallback(value)
                    }
                }
            }
            true
        }
    }

    private fun checkInHeight(y : Float) : Boolean {
        return (y in 0f..maxYPosition.toFloat())
    }

    private fun positionToProgressValue(yPosition: Float): Int {
        val ratio = (maxYPosition - yPosition) / maxYPosition.toFloat()
        return min + ((max - min) * ratio).roundToInt()
    }

    private fun progressValueToPosition(progressValue : Int) : Float {
        val ratio = (progressValue - min) / (max - min).toFloat()
        return (1 - ratio) * maxYPosition
    }

    fun setProgressChangeCallback(callback : (Int) -> Unit) {
        progressValueChangeCallback = callback
    }

    fun setProgressValue(value : Int) {
        val yPosition = progressValueToPosition(value)
        binding.flWriteTodayOnelineSeekbarThumb.y = yPosition
    }

    companion object {
        const val NOT_INIT = -1
        const val NOT_INIT_FLOAT = -1f
    }
}