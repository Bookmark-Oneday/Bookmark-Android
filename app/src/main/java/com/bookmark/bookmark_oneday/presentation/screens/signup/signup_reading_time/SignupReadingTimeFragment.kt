package com.bookmark.bookmark_oneday.presentation.screens.signup.signup_reading_time

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.FragmentSignupReadingTimeBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingFragment
import com.bookmark.bookmark_oneday.presentation.screens.signup.SignupViewModel
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle

class SignupReadingTimeFragment : ViewBindingFragment<FragmentSignupReadingTimeBinding>(FragmentSignupReadingTimeBinding::bind, R.layout.fragment_signup_reading_time) {
    private val viewModel : SignupViewModel by activityViewModels()

    private val minTimeMinute = 10
    private val maxTimeMinute = 240
    private val timeUnit = (maxTimeMinute - minTimeMinute) / 46

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        setProgress()
        setObserver()

        binding.toolbarSignupReadingTime.startProgressAnimation(viewModel.getPrevProgress())
        viewModel.setPrevProgress(75)
    }

    private fun setButton() {
        binding.toolbarSignupReadingTime.setBackButtonEvent {
            binding.root.findNavController().popBackStack()
        }

        binding.btnSignupReadingTimeNext.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_signupReadingTimeFragment_to_signupCompleteFragment)
        }
    }

    private fun setProgress() {
        binding.progressSignupReadingTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setGoalReadingTimeMinute(p1 * timeUnit + minTimeMinute)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun setObserver() {
        viewModel.goalReadingTimeMinute.collectLatestInLifecycle(viewLifecycleOwner) { goalTimeMinute ->
            binding.progressSignupReadingTime.progress = (goalTimeMinute - minTimeMinute) / timeUnit
            setTimeText(goalTimeMinute)
            setTimeCaptionText(goalTimeMinute)
        }
    }

    private fun setTimeText(timeMinute : Int) {
        val hour = timeMinute / 60
        val minute = timeMinute % 60

        val text = if (hour >= 1 && minute == 0) {
            getString(R.string.label_signup_set_reading_time_format_exclude_minute, hour)
        } else if (hour == 0) {
            getString(R.string.label_signup_set_reading_time_format_exclude_hour, minute)
        } else {
            getString(R.string.label_signup_set_reading_time_format, hour, minute)
        }

        binding.labelSignupReadingTimeTime.text = text
    }

    private fun setTimeCaptionText(timeMinute : Int) {
        val averageReadingTime2022 = 30
        val timeDiff = timeMinute - averageReadingTime2022

        val text = if (timeDiff >= 0) {
            HtmlCompat.fromHtml(getString(R.string.html_caption_signup_set_reading_time_more, timeDiff), HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            HtmlCompat.fromHtml(getString(R.string.html_caption_signup_set_reading_time_less, -timeDiff), HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        binding.labelSignupReadingTimeCaption.text = text
    }
}