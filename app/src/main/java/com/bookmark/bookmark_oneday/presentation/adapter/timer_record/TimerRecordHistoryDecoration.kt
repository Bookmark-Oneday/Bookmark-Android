package com.bookmark.bookmark_oneday.presentation.adapter.timer_record

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class TimerRecordHistoryDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val padding = dpToPx(context, 24)
    private val height = dpToPx(context, 1)
    private val paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context, R.color.gray)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = (parent.paddingStart + padding).toFloat()
        val right = (parent.width - parent.paddingEnd - padding).toFloat()

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = (child.bottom + params.bottomMargin).toFloat()
            val bottom = top + height

            c.drawRect(left, top, right, bottom, paint)
        }
    }

}