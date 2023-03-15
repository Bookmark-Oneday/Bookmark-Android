package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogBookdetailBookmarkBinding

class BookDetailEditPageDialog(
    private val onClick : (Int, Int) -> Unit,
    private val currentPage : Int ?= null,
    private val totalPage : Int ?= null
) : DialogFragment() {
    private lateinit var binding : DialogBookdetailBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogBookdetailBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentPage?.let { binding.inputBookdetailCurrentpage.setText(it.toString()) }
        totalPage?.let { binding.inputBookdetailTotalPage.setText(it.toString()) }

        binding.btnBookdetailBookmarkdialogInput.setOnClickListener {
            val inputCurrentPage = binding.inputBookdetailCurrentpage.text.toString().toIntOrNull()
            val inputTotalPage = binding.inputBookdetailTotalPage.text.toString().toIntOrNull()

            if (inputCurrentPage != null && inputTotalPage != null) {
                onClick(inputCurrentPage, inputTotalPage)
                dismiss()
            }

            // 범위 확인 문구 표시
        }
    }


}