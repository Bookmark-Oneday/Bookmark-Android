package com.bookmark.bookmark_oneday.presentation.adapter.reading_history

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class BookDetailReadingHistoryDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val horizontalPadding = dpToPx(context, 24)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position != 0) {
            outRect.left = horizontalPadding
        }
    }
}