package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.PartialWriteTodayOnelineEdittextBinding
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Font

class OnelineEditText(context : Context, attrs : AttributeSet) : FrameLayout(context, attrs) {
    private val binding : PartialWriteTodayOnelineEdittextBinding
    init {
        val inflater = LayoutInflater.from(context)
        binding = PartialWriteTodayOnelineEdittextBinding.inflate(inflater, this, true)
    }

    fun setMaxWidth(maxWidth : Int) {
        binding.edittextWriteTodayOnelineEdit.maxWidth = maxWidth
        binding.labelWriteTodayOnelineText.maxWidth = maxWidth
    }


    fun setOnTextChanged(callback : (String) -> Unit) {
        binding.edittextWriteTodayOnelineEdit.addTextChangedListener { editable ->
            val text = editable?.toString() ?: return@addTextChangedListener
            callback(text)
        }
    }

    fun setReadOnly(readOnly : Boolean) {
        binding.edittextWriteTodayOnelineEdit.visibility = if (readOnly) View.INVISIBLE else View.VISIBLE
        binding.labelWriteTodayOnelineText.visibility = if (readOnly) View.VISIBLE else View.INVISIBLE

        if (readOnly) {
            binding.edittextWriteTodayOnelineEdit.clearFocus()
        } else {
            binding.edittextWriteTodayOnelineEdit.requestFocus()
        }
    }

    fun setTextSize(sp : Int) {
        binding.edittextWriteTodayOnelineEdit.textSize = sp.toFloat()
        binding.labelWriteTodayOnelineText.textSize = sp.toFloat()
    }

    fun setText(text : String) {
        binding.labelWriteTodayOnelineText.text = text.ifEmpty {
            context.getString(R.string.label_write_today_oneline_place_holder)
        }

        if (text == binding.edittextWriteTodayOnelineEdit.text.toString()) return
        binding.edittextWriteTodayOnelineEdit.setText(text)
    }

    fun setTextColor(colorString: String) {
        binding.edittextWriteTodayOnelineEdit.setTextColor(Color.parseColor(colorString))
        binding.labelWriteTodayOnelineText.setTextColor(Color.parseColor(colorString))
    }

    fun setFont(fontResourceId : Int) {
        if (fontResourceId == Font.FONT_DEFAULT) {
            binding.edittextWriteTodayOnelineEdit.typeface = Typeface.DEFAULT
            binding.labelWriteTodayOnelineText.typeface = Typeface.DEFAULT
        } else {
            binding.edittextWriteTodayOnelineEdit.typeface = ResourcesCompat.getFont(context, fontResourceId)
            binding.labelWriteTodayOnelineText.typeface = ResourcesCompat.getFont(context, fontResourceId)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initEditTextOnClick(callback : () -> Unit) {
        binding.edittextWriteTodayOnelineEdit.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (v.hasFocus()) return@setOnTouchListener true

                    callback()
                }
            }
            return@setOnTouchListener true
        }
    }

    fun clearEditTextFocus() {
        binding.edittextWriteTodayOnelineEdit.clearFocus()
    }
}