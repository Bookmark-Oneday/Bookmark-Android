package com.bookmark.bookmark_oneday.presentation.screens.timer.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bookmark.bookmark_oneday.R

class ToggleButton(context : Context, attrs : AttributeSet) : View(context, attrs) {

    private val toggleOffBackground : Int
    private val toggleOnBackground : Int
    private var toggle : Boolean = false

    private var toggleOnClick : () -> Unit = {}
    private var toggleOffClick : () -> Unit = {}

    private var downTouch : Boolean = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ToggleButton, 0, 0
        ).apply {
            try {
                toggleOnBackground = getResourceId(R.styleable.ToggleButton_toggleOnBackground, 0)
                toggleOffBackground = getResourceId(R.styleable.ToggleButton_toggleOffBackground, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val backgroundId = if (toggle) toggleOnBackground else toggleOffBackground
        setBackgroundResource(backgroundId)
    }

    fun setToggleState(isToggle : Boolean) {
        toggle = isToggle
        invalidate()
    }

    fun setToggleOnClick(onClick : () -> Unit) {
        toggleOnClick = onClick
    }

    fun setToggleOffClick(onClick : () -> Unit) {
        toggleOffClick = onClick
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downTouch = true
                true
            }
            MotionEvent.ACTION_UP -> if (downTouch) {
                downTouch = false
                performClick()
            } else {
                false
            }
            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()

        if (toggle) { toggleOnClick() }
        else { toggleOffClick() }

        return true
    }
}