package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.PartialWriteTodayOnelineTextAttrSettingBinding
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineColorAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineColorDecoration
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineFontAdapter
import com.bookmark.bookmark_oneday.presentation.adapter.write_today_oneline.WriteTodayOnelineFontDecoration
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model.Font

class OnelineTextAttrSettingView(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding : PartialWriteTodayOnelineTextAttrSettingBinding
    private var fontViewHeight = 0

    init {
        val inflater = LayoutInflater.from(context)
        binding = PartialWriteTodayOnelineTextAttrSettingBinding.inflate(inflater, this, true)

        binding.listFont.layoutManager = GridLayoutManager(context, 2)
        binding.listFont.adapter = WriteTodayOnelineFontAdapter()
        binding.listFont.addItemDecoration(WriteTodayOnelineFontDecoration(context))

        binding.listColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.listColor.adapter = WriteTodayOnelineColorAdapter()
        binding.listColor.addItemDecoration(WriteTodayOnelineColorDecoration(context))

        binding.btnColor.setOnClickListener {
            showColorSelectView()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        fontViewHeight = binding.listFont.measuredHeight
    }

    private fun showColorSelectView() {
        binding.imgColorStroke.visibility = View.INVISIBLE
        binding.btnColor.visibility = View.INVISIBLE
        binding.lineSeparatorFontColor.visibility = View.INVISIBLE
        binding.btnFont.visibility = View.INVISIBLE
        binding.listColor.visibility = View.VISIBLE
    }

    fun getFontViewHeight() = fontViewHeight

    fun toHideMode() {
        binding.btnFont.setTextColor(ContextCompat.getColor(binding.root.context, R.color.default_text))
    }

    fun toSoftKeyboardMode() {
        binding.btnFont.setTextColor(ContextCompat.getColor(binding.root.context, R.color.default_text))
    }

    fun toFontSettingMode() {
        binding.btnFont.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange))
    }

    fun setSelectedColor(colorString : String) {
        binding.btnColor.imageTintList = ColorStateList.valueOf(Color.parseColor(colorString))
    }

    fun setColorChangeCallback(callback : (String) -> Unit) {
        (binding.listColor.adapter as WriteTodayOnelineColorAdapter).setColorChangeCallback{ colorString ->
            callback(colorString)
            hideColorSelectView()
        }
    }

    fun hideColorSelectView() {
        binding.imgColorStroke.visibility = View.VISIBLE
        binding.btnColor.visibility = View.VISIBLE
        binding.lineSeparatorFontColor.visibility = View.VISIBLE
        binding.btnFont.visibility = View.VISIBLE
        binding.listColor.visibility = View.INVISIBLE
    }

    fun setFontChangeCallback(callback : (Font) -> Unit) {
        (binding.listFont.adapter as WriteTodayOnelineFontAdapter).setFontChangeCallback(callback)
    }

    fun setFontButtonClick(callback : () -> Unit) {
        binding.btnFont.setOnClickListener { callback() }
    }

    fun setFont(font : Font) {
        (binding.listFont.adapter as WriteTodayOnelineFontAdapter).changeSelectedFont(font)
    }
}