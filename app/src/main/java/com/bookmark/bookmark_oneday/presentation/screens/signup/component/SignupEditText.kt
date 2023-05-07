package com.bookmark.bookmark_oneday.presentation.screens.signup.component

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ViewSignupEdittextBinding

class SignupEditText(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding : ViewSignupEdittextBinding
    private val max : Int
    private val placeHolder : String
    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewSignupEdittextBinding.inflate(layoutInflater, this, true)

        context.theme.obtainStyledAttributes(attrs, R.styleable.SignupEditText, 0, 0).run {
            try {
                max = getInteger(R.styleable.SignupEditText_max, 10)
                placeHolder = getString(R.styleable.SignupEditText_placeHolder) ?: "내용 입력"
                binding.inputSignup.filters = arrayOf(InputFilter.LengthFilter(max))
                binding.inputSignup.hint = placeHolder
                binding.labelSignupTextCount.text = context.getString(R.string.label_signup_text_count_format, 0, max)
            } finally {
                recycle()
            }
        }

    }

    fun setText(text : String) {
        val currentText = binding.inputSignup.text.toString()
        if (currentText != text) {
            binding.inputSignup.setText(text)
        }
    }

    fun setTextChangedListener(setText : (String) -> Unit) {
        binding.inputSignup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    setText(it.toString())
                    binding.labelSignupTextCount.text = context.getString(R.string.label_signup_text_count_format, it.length, max)
                }
            }
        })
    }
}