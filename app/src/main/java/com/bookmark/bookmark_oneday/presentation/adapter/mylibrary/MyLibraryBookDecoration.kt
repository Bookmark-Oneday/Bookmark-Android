package com.bookmark.bookmark_oneday.presentation.adapter.mylibrary

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bookmark.bookmark_oneday.presentation.util.dpToPx

class MyLibraryBookDecoration(context : Context)  : RecyclerView.ItemDecoration() {
    private val horizontalPadding = dpToPx(context, 8)
    private val bottomPadding = dpToPx(context, 24)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalPadding
        outRect.right = horizontalPadding
        outRect.bottom = bottomPadding
    }
}