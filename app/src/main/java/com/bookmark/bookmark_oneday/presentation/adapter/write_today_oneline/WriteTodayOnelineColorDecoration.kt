package com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class WriteTodayOnelineColorDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val interval = dpToPx(context, 8)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        if (position == 0) {
            outRect.left = interval
        }
        outRect.right = interval
    }
}