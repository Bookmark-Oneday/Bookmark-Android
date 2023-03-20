package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.component

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogBookConfirmDuplicateBinding

class BookConfirmDuplicateDialog(
    private val onClose : () -> Unit,
    private val onClickRegister : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogBookConfirmDuplicateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogBookConfirmDuplicateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBookconfirmDuplicatedialogCancel.setOnClickListener {
            dismiss()
        }
        binding.btnBookconfirmDuplicatedialogRegister.setOnClickListener {
            dismiss()
            onClickRegister()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onClose()
    }
}