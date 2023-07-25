package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_cancel_timer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogTimerCancelBinding

class CancelTimerDialog(
    private val onClickBack : () -> Unit
) : DialogFragment() {

    private lateinit var binding : DialogTimerCancelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogTimerCancelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
    }

    private fun initButton() {
        binding.btnTimerCancelDialogYes.setOnClickListener {
            onClickBack()
            dismiss()
        }

        binding.btnTimerCancelDialogNot.setOnClickListener {
            dismiss()
        }
    }
}