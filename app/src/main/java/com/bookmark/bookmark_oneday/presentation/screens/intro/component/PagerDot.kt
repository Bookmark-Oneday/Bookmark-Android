package com.bookmark.bookmark_oneday.presentation.screens.intro.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class PagerDot(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {
    private val dotViewList = mutableListOf<View>()
    init {
        orientation = HORIZONTAL
    }

    fun setTotalSize(size : Int) {
        val dotSize = dpToPx(context, 8)
        val marginSize = dotSize / 2

        val layoutParams = LayoutParams(dotSize, dotSize)
        layoutParams.setMargins(marginSize, 0, marginSize, 0)

        for (i in 0 until size) {
            val view = View(context)
            view.layoutParams = layoutParams
            view.background = AppCompatResources.getDrawable(context, R.drawable.intro_dot)
            dotViewList.add(view)
            addView(view)
        }
    }

    fun setActivePosition(position : Int) {
        val total = dotViewList.size
        for (i in 0 until total) {
            val selected = (position == i)
            dotViewList[i].isEnabled = selected
        }

        invalidate()
    }
}