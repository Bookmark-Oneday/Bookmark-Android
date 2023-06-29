package com.bookmark.bookmark_oneday.presentation.screens.home.mypage.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogConfirmClearDataBinding

class ConfirmClearDataDialog(
    private val onClickClear : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogConfirmClearDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogConfirmClearDataBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
    }

    private fun setButton(){
        binding.btnConfirmClearClear.setOnClickListener {
            onClickClear()
            dismiss()
        }

        binding.btnConfirmClearCancel.setOnClickListener {
            dismiss()
        }
    }
}