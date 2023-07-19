package com.bookmark.bookmark_oneday.presentation.adapter.reading_calendar

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class ReadingCalendarItemDecoration(context : Context) : ItemDecoration() {
    private val horizontalInterval = dpToPx(context, 2)
    private val verticalInterval = dpToPx(context, 4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        outRect.left = horizontalInterval
        outRect.right = horizontalInterval
        outRect.bottom = verticalInterval

        if (position / 7 != 0) {
            outRect.top = verticalInterval
        }
    }
}