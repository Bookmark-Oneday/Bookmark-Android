package com.bookmark.bookmark_oneday.presentation.screens.modify_reading_time

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.databinding.ActivityModifyReadingTimeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import com.bookmark.bookmark_oneday.presentation.util.getReadingTimeString
import com.bookmark.bookmark_oneday.presentation.util.getTimeCaptionHtmlText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyReadingTimeActivity : ViewBindingActivity<ActivityModifyReadingTimeBinding>(ActivityModifyReadingTimeBinding::inflate) {
    private val viewModel : ModifyReadingTimeViewModel by viewModels()

    private val minTimeMinute = 10
    private val maxTimeMinute = 240
    private val timeUnit = (maxTimeMinute - minTimeMinute) / 46

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setObserver()
        setProgress()
    }

    private fun setButton() {
        binding.btnModifyReadingTimeBack.setOnClickListener {
            finish()
        }

        binding.btnModifyReadingTimeComplete.setOnClickListener {
            viewModel.modifyGoalReadingTime()
        }
    }

    private fun setObserver() {
        viewModel.inputReadingTime.collectLatestInLifecycle(this) { goalTimeMinute ->
            binding.progressModifyReadingTimeReadingTime.progress = (goalTimeMinute - minTimeMinute) / timeUnit
            setTimeText(goalTimeMinute)
            setTimeCaptionText(goalTimeMinute)
        }

        viewModel.modifyReadingTimeResult.collectLatestInLifecycle(this) { modifySuccess ->
            if (modifySuccess) {
                finish()
            }
        }

    }

    private fun setTimeText(timeMinute : Int) {
        val text = getReadingTimeString(this, timeMinute)
        binding.labelModifyReadingTimeTime.text = text
    }

    private fun setTimeCaptionText(timeMinute : Int) {
        val spanned = getTimeCaptionHtmlText(this, timeMinute)
        binding.labelModifyReadingTimeCaption.text = spanned
    }

    private fun setProgress() {
        binding.progressModifyReadingTimeReadingTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setInputReadingTime(p1 * timeUnit + minTimeMinute)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }
}