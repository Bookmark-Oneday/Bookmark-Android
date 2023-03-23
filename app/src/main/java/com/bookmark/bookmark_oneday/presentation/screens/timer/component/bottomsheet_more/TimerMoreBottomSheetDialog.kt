package com.bookmark.bookmark_oneday.presentation.screens.timer.component.bottomsheet_more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookmark.bookmark_oneday.databinding.BottomsheetTimerMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimerMoreBottomSheetDialog(
    private val onClickRemove : () -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var binding : BottomsheetTimerMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetTimerMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTimerBottomsheetRemoveAll.setOnClickListener {
            dismiss()
            onClickRemove()
        }
    }
}