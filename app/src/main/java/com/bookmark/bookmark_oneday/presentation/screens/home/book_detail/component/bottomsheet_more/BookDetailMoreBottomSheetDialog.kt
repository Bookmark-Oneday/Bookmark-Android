package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.bottomsheet_more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookmark.bookmark_oneday.databinding.BottomsheetBookdetailMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookDetailMoreBottomSheetDialog(
    private val onClickRemove : () -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var binding : BottomsheetBookdetailMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetBookdetailMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBookdetailRemoveFromBookList.setOnClickListener {
            dismiss()
            onClickRemove()
        }
    }
}