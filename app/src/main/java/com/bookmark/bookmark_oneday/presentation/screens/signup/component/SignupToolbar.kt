package com.bookmark.bookmark_oneday.presentation.screens.signup.component

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.PartialSignupToolbarBinding

class SignupToolbar(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding : PartialSignupToolbarBinding
    private val progress : Int

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = PartialSignupToolbarBinding.inflate(layoutInflater, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SignupToolbar, 0, 0
        ).run {
            try {
                progress = getInteger(R.styleable.SignupToolbar_progress, 0)
            } finally {
                recycle()
            }
        }
    }

    fun setBackButtonEvent(event : (() -> Unit)?) {
        if (event != null) {
            binding.btnSignupBack.visibility = View.VISIBLE
            binding.btnSignupBack.setOnClickListener { event() }
        } else {
            binding.btnSignupBack.visibility = View.INVISIBLE
        }
    }

    fun startProgressAnimation(startProgress : Int) {
        // startProgress 에서 progress 로 애니메이션 수행
        val animator = ObjectAnimator.ofInt(binding.progressSignup, "progress", startProgress, progress)
        animator.duration = 500L
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

}