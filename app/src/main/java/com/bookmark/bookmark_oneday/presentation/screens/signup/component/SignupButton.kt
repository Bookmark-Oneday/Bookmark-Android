package com.bookmark.bookmark_oneday.presentation.screens.signup.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ViewSignupButtonBinding

class SignupButton(context: Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding : ViewSignupButtonBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewSignupButtonBinding.inflate(layoutInflater, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SignupButton, 0, 0
        ).run {
            try {
                val text = getString(R.styleable.SignupButton_text)
                binding.labelSignupButton.text = text
            } finally {
                recycle()
            }
        }
    }

    fun setOnClickListener(onClick : () -> Unit) {
        binding.rootSignupButton.setOnClickListener { onClick() }
    }

    fun setButtonEnabled(enable : Boolean) {
        binding.rootSignupButton.isEnabled = enable
    }
}