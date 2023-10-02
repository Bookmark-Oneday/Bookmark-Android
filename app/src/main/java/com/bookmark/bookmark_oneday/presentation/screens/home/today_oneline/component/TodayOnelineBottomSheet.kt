package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookmark.bookmark_oneday.databinding.BottomsheetTodayOnelineMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodayOnelineBottomSheet(
    private val isWriter : Boolean = true,
    private val onRemoveClick : () -> Unit = {},
    private val onReportClick : () -> Unit = {},
    private val onBookInfoClick : () -> Unit = {}
) : BottomSheetDialogFragment() {
    private lateinit var binding : BottomsheetTodayOnelineMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetTodayOnelineMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
    }

    private fun initButton() {
        if (isWriter) {
            binding.btnOnelineBottomsheetRemove.visibility = View.VISIBLE
            binding.btnOnelineBottomsheetRemove.setOnClickListener {
                onRemoveClick()
                dismiss()
            }
        } else {
            binding.btnOnelineBottomsheetReport.visibility = View.VISIBLE
            binding.btnOnelineBottomsheetReport.setOnClickListener {
                onReportClick()
                dismiss()
            }
        }

        binding.btnOnelineBottomsheetBookInfo.setOnClickListener {
            onBookInfoClick()
            dismiss()
        }
    }
}