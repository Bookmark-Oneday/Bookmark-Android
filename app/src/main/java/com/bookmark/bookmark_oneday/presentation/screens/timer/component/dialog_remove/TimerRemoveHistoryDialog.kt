package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogTimerRemoveHistoryBinding

class TimerRemoveHistoryDialog : DialogFragment() {

    private lateinit var binding : DialogTimerRemoveHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogTimerRemoveHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
    }

    private fun setButton() {
        binding.btnTimerRemoveHistoryDialogRemove.setOnClickListener {
            dismiss()
        }

        binding.btnTimerRemoveHistoryDialogCancel.setOnClickListener {
            dismiss()
        }
    }

}