package com.bookmark.bookmark_oneday.presentation.util

import kotlin.math.abs

interface DragChecker{
    fun saveActionDownPosition(x : Float = 0f, y : Float = 0f)
    fun checkIsDragged(x : Float = 0f, y : Float = 0f) : Boolean
}

class VerticalDragChecker(
    private val threshold : Int = 5
) : DragChecker {
    private var downY = 0f

    override fun saveActionDownPosition(x: Float, y: Float) {
        downY = y
    }

    override fun checkIsDragged(x: Float, y: Float): Boolean {
        return abs(downY - y).toInt() > threshold
    }

}