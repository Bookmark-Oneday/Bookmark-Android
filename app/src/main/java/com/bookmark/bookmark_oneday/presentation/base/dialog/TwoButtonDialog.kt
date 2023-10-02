package com.bookmark.bookmark_oneday.presentation.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.DialogAllTwoButtonBinding

class TwoButtonDialog(
    private val title : String,
    private val caption : String,
    private val leftText : String,
    private val rightText : String,
    private val onLeftButtonClick : () -> Unit,
    private val onRightButtonClick : () -> Unit,
    private val accentLeft : Boolean = false
) : DialogFragment() {

    private lateinit var binding : DialogAllTwoButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogAllTwoButtonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextView()
        initButton()
    }

    private fun initTextView() {
        binding.labelAllTwoButtonDialogTitle.text = title
        binding.labelAllTwoButtonDialogCaption.text = caption

        binding.btnAllTwoButtonDialogLeft.text = leftText
        binding.btnAllTwoButtonDialogRight.text = rightText
    }

    private fun initButton() {
        binding.btnAllTwoButtonDialogLeft.setOnClickListener {
            onLeftButtonClick()
            dismiss()
        }

        binding.btnAllTwoButtonDialogRight.setOnClickListener {
            onRightButtonClick()
            dismiss()
        }

        if (accentLeft) {
            binding.btnAllTwoButtonDialogLeft.background = ContextCompat.getDrawable(binding.root.context, R.drawable.all_roundbutton_positive)
            binding.btnAllTwoButtonDialogRight.background = ContextCompat.getDrawable(binding.root.context, R.drawable.all_roundbutton_negative)
        } else {
            binding.btnAllTwoButtonDialogLeft.background = ContextCompat.getDrawable(binding.root.context, R.drawable.all_roundbutton_negative)
            binding.btnAllTwoButtonDialogRight.background = ContextCompat.getDrawable(binding.root.context, R.drawable.all_roundbutton_positive)
        }
    }
}