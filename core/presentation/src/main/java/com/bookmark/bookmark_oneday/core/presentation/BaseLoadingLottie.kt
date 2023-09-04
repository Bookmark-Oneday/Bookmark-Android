package com.bookmark.bookmark_oneday.core.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bookmark.bookmark_oneday.core.presentation.databinding.BaseLoadingLottieBinding

class BaseLoadingLottie(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding : BaseLoadingLottieBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = BaseLoadingLottieBinding.inflate(layoutInflater, this, true)
    }

    fun setLoadingVisibility(show : Boolean) {
        if (show) {
            binding.progressTodayOnelineLoading.visibility = View.VISIBLE
            binding.progressTodayOnelineLoading.playAnimation()
        } else {
            binding.progressTodayOnelineLoading.visibility = View.INVISIBLE
            binding.progressTodayOnelineLoading.pauseAnimation()
        }
    }

}