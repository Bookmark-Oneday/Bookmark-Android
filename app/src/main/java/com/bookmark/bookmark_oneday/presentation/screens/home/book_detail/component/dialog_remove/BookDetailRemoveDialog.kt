package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogBookdetailRemoveBinding

class BookDetailRemoveDialog(
    private val onRemoveClick : () -> Unit = {}
) : DialogFragment() {

    private lateinit var binding : DialogBookdetailRemoveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogBookdetailRemoveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBookdetailRemovedialogRemove.setOnClickListener{
            dismiss()
            onRemoveClick()
        }
    }
}