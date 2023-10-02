package com.bookmark.bookmark_oneday.presentation.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogAllOneButtonBinding

class OneButtonDialog(
    private val title : String,
    private val caption : String,
    private val buttonText : String,
    private val buttonClick : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogAllOneButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogAllOneButtonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextView()
        initButton()
    }

    private fun initTextView() {
        binding.labelAllOneButtonDialogTitle.text = title
        binding.labelAllOneButtonDialogCaption.text = caption

        binding.btnAllOneButtonDialog.text = buttonText
    }

    private fun initButton() {
        binding.btnAllOneButtonDialog.setOnClickListener {
            dismiss()
            buttonClick()
        }
    }
}