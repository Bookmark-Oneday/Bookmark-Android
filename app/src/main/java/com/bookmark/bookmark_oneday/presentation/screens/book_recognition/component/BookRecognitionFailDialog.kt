package com.bookmark.bookmark_oneday.presentation.screens.book_recognition.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogBookRecognitionFailBinding

class BookRecognitionFailDialog(
    private val onClose : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogBookRecognitionFailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogBookRecognitionFailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun dismiss() {
        super.dismiss()
        onClose()
    }
}