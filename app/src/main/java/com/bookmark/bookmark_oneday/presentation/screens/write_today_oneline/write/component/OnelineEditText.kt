package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.PartialWriteTodayOnelineEdittextBinding

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
        binding.edittextWriteTodayOnelineEdit.typeface = ResourcesCompat.getFont(context, fontResourceId)
        binding.labelWriteTodayOnelineText.typeface = ResourcesCompat.getFont(context, fontResourceId)
    }

    fun initEditTextOnClick(callback : () -> Unit) {
        binding.edittextWriteTodayOnelineEdit.setOnClickListener {
            callback()
        }
    }

    fun clearEditTextFocus() {
        binding.edittextWriteTodayOnelineEdit.clearFocus()
    }
}