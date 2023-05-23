package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.IBinder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.bookmark.bookmark_oneday.R
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

    private var longClick = false
    private var moveAvailable = true

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

        binding.edittextWriteTodayOnelineContent.setMaxWidth(measuredWidth)
        binding.clWriteTodayOnelineContent.maxWidth = layoutWidth
    }

    fun initModeSwitchEvent(callback : () -> Unit) {
        binding.clWriteTodayOnelineContent.setOnClickListener {
            callback()
        }
    }

    fun setToMoveMode() {
        binding.edittextWriteTodayOnelineContent.setReadOnly(readOnly = true)
        moveAvailable = true
    }

    fun setToEditMode() {
        binding.edittextWriteTodayOnelineContent.setReadOnly(readOnly = false)
        moveAvailable = false
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initOnPositionChanged(callback : (Float, Float) -> Unit) {
        binding.clWriteTodayOnelineContent.setOnLongClickListener {
            if (!moveAvailable) return@setOnLongClickListener false
            setMoveUiVisibility(show = true)
            longClick = true
            return@setOnLongClickListener true
        }

        binding.clWriteTodayOnelineContent.setOnTouchListener { view, event ->
            if (!moveAvailable) return@setOnTouchListener false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    xCursorDown = view.x - event.rawX
                    yCursorDown = view.y - event.rawY

                    // textView, EditTextView 는 Text 를 입력할 때마다 너비가 바뀔 수 있습니다.
                    contentViewWidth = binding.clWriteTodayOnelineContent.measuredWidth
                    contentViewHeight = binding.clWriteTodayOnelineContent.measuredHeight

                    if (!longClick) return@setOnTouchListener false
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!longClick) return@setOnTouchListener false

                    val newX = event.rawX + xCursorDown
                    if (checkInLayoutWidth(newX)) {
                        binding.clWriteTodayOnelineContent.x = newX
                    }

                    val newY = event.rawY + yCursorDown
                    if (checkInLayoutHeight(newY)) {
                        binding.clWriteTodayOnelineContent.y = newY
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

                    if (longClick) {
                        longClick = false
                        setMoveUiVisibility(show = false)
                    }
                    return@setOnTouchListener false
                }
            }
            true
        }
    }

    // text 위치를 변경할 때 사용되는 UI 들의 표시 여부를 변경합니다.
    private fun setMoveUiVisibility(show : Boolean) {
        if (show) {
            binding.llWriteTodayOnelineContent.setBackgroundResource(R.drawable.oneline_content_background)
            binding.imgWriteTodayOnelineMove.visibility = View.VISIBLE
        } else {
            binding.llWriteTodayOnelineContent.setBackgroundResource(0)
            binding.imgWriteTodayOnelineMove.visibility = View.INVISIBLE
            binding.viewWriteTodayOnelineVerticalCenter.visibility = View.INVISIBLE
            binding.viewWriteTodayOnelineHorizontalCenter.visibility = View.INVISIBLE
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
        binding.edittextWriteTodayOnelineContent.setOnTextChanged(callback)
    }

    fun setTextColor(colorString : String) {
        binding.labelWriteTodayOnelineBookInfo.setTextColor(Color.parseColor(colorString))
        binding.edittextWriteTodayOnelineContent.setTextColor(colorString)
    }

    fun setTextSize(sp : Int) {
        val captionTextSize = sp * 70 / 100
        binding.labelWriteTodayOnelineBookInfo.textSize = captionTextSize.toFloat()
        binding.edittextWriteTodayOnelineContent.setTextSize(sp)
    }

    fun setText(text : String) {
        binding.edittextWriteTodayOnelineContent.setText(text)
    }

    fun setBookText(text : String) {
        binding.labelWriteTodayOnelineBookInfo.text = text
    }

    fun setEditTextFocus(focus : Boolean) {
        if (focus) {
            binding.edittextWriteTodayOnelineContent.requestFocus()
        } else {
            binding.edittextWriteTodayOnelineContent.clearFocus()
        }
    }

    /** content view 의 위치를 비율값을 사용해 조정합니다.
     *
     * xRatio : 전체 view 에서 x 좌표의 비율값 ( = xPosition / layoutWitdh ),
     * yRatio : 전체 view 에서 y 좌표의 비율값 ( = yPosition / layoutHeight )
     */
    fun setPosition(xRatio : Float, yRatio : Float) {
        val positionX = layoutWidth * xRatio - contentViewWidth / 2
        val positionY = layoutHeight * yRatio - contentViewHeight / 2

        binding.clWriteTodayOnelineContent.x = positionX
        binding.clWriteTodayOnelineContent.y = positionY
    }

    fun setFont(fontResourceId : Int) {
        binding.edittextWriteTodayOnelineContent.setFont(fontResourceId)
    }

    fun getFocusViewWindowToken(): IBinder = binding.edittextWriteTodayOnelineContent.windowToken

    companion object {
        const val NOT_INIT = -1
        const val NOT_INIT_FLOAT = -1f
    }
}