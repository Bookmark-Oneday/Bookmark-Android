package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.bookmark.bookmark_oneday.databinding.PartialWriteTodayOnelineContentBinding
import com.bookmark.bookmark_oneday.presentation.util.dpToPx
import kotlin.math.max

class OnelineContentView(context : Context, attrs : AttributeSet) : FrameLayout(context, attrs) {

    private val binding : PartialWriteTodayOnelineContentBinding
    private var layoutWidth = NOT_INIT
    private var layoutHeight = NOT_INIT

    private var contentViewWidth = NOT_INIT
    private var contentViewHeight = NOT_INIT

    private var xCursorDown = NOT_INIT_FLOAT
    private var yCursorDown = NOT_INIT_FLOAT

    private val centerInterval = dpToPx(context, 10)

    init {
        val inflater = LayoutInflater.from(context)
        binding = PartialWriteTodayOnelineContentBinding.inflate(inflater, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        layoutWidth = measuredWidth
        layoutHeight = measuredHeight

        contentViewWidth = binding.clWriteTodayOnelineContent.measuredWidth
        contentViewHeight = binding.clWriteTodayOnelineContent.measuredHeight

        binding.edittextWriteTodayOnelineContent.maxWidth = measuredWidth
        binding.clWriteTodayOnelineContent.maxWidth = layoutWidth
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnPositionChanged(callback : (Float, Float) -> Unit) {
        binding.clWriteTodayOnelineContent.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    xCursorDown = view.x - event.rawX
                    yCursorDown = view.y - event.rawY

                    // textView, EditTextView 는 Text 를 입력할 때마다 너비가 바뀔 수 있습니다.
                    contentViewWidth = binding.clWriteTodayOnelineContent.measuredWidth
                    contentViewHeight = binding.clWriteTodayOnelineContent.measuredHeight
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + xCursorDown
                    if (checkInLayoutWidth(newX)) {
                        binding.clWriteTodayOnelineContent.x = event.rawX + xCursorDown
                    }

                    val newY = event.rawY + yCursorDown
                    if (checkInLayoutHeight(newY)) {
                        binding.clWriteTodayOnelineContent.y = event.rawY + yCursorDown
                    }

                    binding.viewWriteTodayOnelineHorizontalCenter.visibility =
                        if (checkInHorizontalCenterRange()) { View.VISIBLE } else { View.INVISIBLE }

                    binding.viewWriteTodayOnelineVerticalCenter.visibility =
                        if (checkInVerticalCenterRange()) { View.VISIBLE } else { View.INVISIBLE }
                }
                MotionEvent.ACTION_UP -> {
                    val inHorizontalCenterRange = checkInHorizontalCenterRange()
                    val inVerticalHorizontalCenterRange = checkInVerticalCenterRange()

                    moveToAxiosCenter(
                        moveHorizontal = inHorizontalCenterRange,
                        moveVertical = inVerticalHorizontalCenterRange
                    )

                    callback(
                        (binding.clWriteTodayOnelineContent.x + contentViewWidth / 2) / layoutWidth,
                        (binding.clWriteTodayOnelineContent.y + contentViewHeight / 2)  / layoutHeight
                    )

                    binding.viewWriteTodayOnelineVerticalCenter.visibility = View.INVISIBLE
                    binding.viewWriteTodayOnelineHorizontalCenter.visibility = View.INVISIBLE

                    view.performClick()
                }
            }
            true
        }
    }

    // 움직인 후의 content view 가 전체 layout 가로 범위 내 존재하는지 검사합니다.
    private fun checkInLayoutWidth(x : Float) : Boolean {
        return (x in 0f..(layoutWidth - contentViewWidth).toFloat())
    }

    // 움직인 후의 content view 가 전체 layout 세로 범위 내 존재하는지 검사합니다.
    private fun checkInLayoutHeight(y : Float) : Boolean {
        return (y in 0f..(layoutHeight - contentViewHeight).toFloat())
    }

    // 현재 content view 의 x 좌표가 가로축의 중앙 범위에 위치하는지 검사합니다.
    private fun checkInHorizontalCenterRange() : Boolean {
        val viewCenterX = binding.clWriteTodayOnelineContent.x.toInt() + (contentViewWidth / 2)
        val layoutCenterX = layoutWidth / 2

        return (viewCenterX in (layoutCenterX - centerInterval)..(layoutCenterX + centerInterval))
    }

    // 현재 content view 의 y 좌표가 세로축의 중앙 범위에 위치하는지 검사합니다.
    private fun checkInVerticalCenterRange() : Boolean {
        val viewCenterY = binding.clWriteTodayOnelineContent.y.toInt() + (contentViewHeight / 2)
        val layoutCenterY = layoutHeight / 2

        return (viewCenterY in (layoutCenterY - centerInterval)..(layoutCenterY + centerInterval))
    }

    /**
     * content view 를 각 축의 중앙으로 이동시킵니다.
     *
     * moveHorizontal : x 축 중앙 좌표로 이동여부,
     * moveVertical : y 축 중앙 좌료로 이동여부
     */
    private fun moveToAxiosCenter(moveHorizontal : Boolean, moveVertical : Boolean) {
        if (moveHorizontal) {
            val targetX = max((layoutWidth / 2 - contentViewWidth / 2).toFloat() , 0f)
            binding.clWriteTodayOnelineContent.x = targetX
        }
        if (moveVertical) {
            val targetY = max((layoutHeight / 2 - contentViewHeight / 2).toFloat(), 0f)
            binding.clWriteTodayOnelineContent.y = targetY
        }
    }

    fun setOnTextChanged(callback : (String) -> Unit) {
        binding.edittextWriteTodayOnelineContent.addTextChangedListener { editable ->
            val text = editable?.toString() ?: return@addTextChangedListener
            callback(text)
        }
    }

    fun setTextColor(colorString : String) {
        binding.labelWriteTodayOnelineBookInfo.setTextColor(Color.parseColor(colorString))
        binding.edittextWriteTodayOnelineContent.setTextColor(Color.parseColor(colorString))
    }

    fun setTextSize(sp : Int) {
        val captionTextSize = sp * 70 / 100
        binding.labelWriteTodayOnelineBookInfo.textSize = captionTextSize.toFloat()
        binding.edittextWriteTodayOnelineContent.textSize = sp.toFloat()
    }

    fun setText(text : String) {
        if (text == binding.edittextWriteTodayOnelineContent.text.toString()) return
        binding.edittextWriteTodayOnelineContent.setText(text)
    }

    fun setBookText(text : String) {
        binding.labelWriteTodayOnelineBookInfo.text = text
    }

    /** content view 의 위치를 비율값을 사용해 조정합니다.
     *
     * xRatio : 전체 view 에서 x 좌표의 비율값 ( = xPosition / layoutWitdh ),
     * yRatio : 전체 view 에서 y 좌표의 비율값 ( = yPosition / layoutHeight )
     */
    fun setPosition(xRatio : Float, yRatio : Float) {
        val positionX = layoutWidth * xRatio - contentViewWidth / 2
        val positionY = layoutHeight * yRatio - contentViewHeight / 2

        println("position ->> $positionX $positionY")
        binding.clWriteTodayOnelineContent.x = positionX
        binding.clWriteTodayOnelineContent.y = positionY
    }

    fun setReadOnly(readOnly : Boolean) {
        binding.edittextWriteTodayOnelineContent.isFocusable = !readOnly
        binding.edittextWriteTodayOnelineContent.isFocusableInTouchMode = !readOnly
    }

    fun setFont(fontResourceId : Int) {
        binding.edittextWriteTodayOnelineContent.typeface = ResourcesCompat.getFont(context, fontResourceId)
        binding.labelWriteTodayOnelineBookInfo.typeface = ResourcesCompat.getFont(context, fontResourceId)
    }

    companion object {
        const val NOT_INIT = -1
        const val NOT_INIT_FLOAT = -1f
    }
}