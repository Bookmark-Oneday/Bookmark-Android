package com.bookmark.bookmark_oneday.presentation.screens.timer.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.presentation.util.dpToPx
import kotlin.math.min

class StopWatchView(context : Context, attrs : AttributeSet) : View(context, attrs) {
    private val strokeWidth = dpToPx(context, 12).toFloat()

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@StopWatchView.strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.orange)
    }
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@StopWatchView.strokeWidth
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.gray)
    }

    private val rect = RectF()
    private val startAngle = -200f
    private val maxAngle = 220f
    private val maxProgress = 100

    private var diameter = 0f
    private var angle = 100f

    override fun onDraw(canvas: Canvas) {
        drawCircle(maxAngle, canvas, backgroundPaint)
        drawCircle(angle, canvas, progressPaint)
    }

    private fun drawCircle(angle : Float, canvas : Canvas, paint : Paint) {
        canvas.drawArc(rect, startAngle, angle, false, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        diameter = w.coerceAtMost(h).toFloat()
        updateRect()
    }

    private fun updateRect() {
        val strokeWidth = backgroundPaint.strokeWidth
        rect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
    }

    fun setProgress(progress : Float) {
        angle = calculateAngle(progress)
        invalidate()
    }

    fun setProgress(progress : Int) {
        setProgress(progress.toFloat())
    }

    private fun calculateAngle(progress : Float) = maxAngle / maxProgress * min(progress, 100f)
}
